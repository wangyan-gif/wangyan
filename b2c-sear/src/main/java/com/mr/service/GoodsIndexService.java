package com.mr.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodsClient;
import com.mr.client.SpecClient;
import com.mr.common.utils.JsonUtils;
import com.mr.common.utils.PageResult;
import com.mr.dao.GoodsRepository;
import com.mr.dao.HignLightUtil;
import com.mr.pojo.Goods;
import com.mr.service.bo.SearchPageBo;
import com.mr.service.pojo.*;
import com.mr.utils.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsIndexService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private GoodsRepository goodsRepository;
    //注入ElasticsearchTemplate进行高亮查询
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Goods buildGoodsBySpu(Spu spu){

        Goods goods = new Goods();
        //填充spu属性  good属性重合
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setSubTitle(spu.getSubTitle());
        goods.setCreateTime(spu.getCreateTime());

        //填充all
        Brand brand = brandClient.queryBrandById(spu.getBrandId());

        //查询三级分类
        List<Category> categoryList = categoryClient
                .queryCateListByIds(
                        Arrays.asList(
                                spu.getCid1(),
                                spu.getCid2(),
                                spu.getCid3()));
        List<String> cateNameList = categoryList.stream().map(category -> {
            return category.getName();
        }).collect(Collectors.toList());
        //填充完毕all
        goods.setAll(spu.getTitle()+" "+brand.getName()+cateNameList.toString());

        //填充价格
        List<Long> priceList = new ArrayList<>();
        //填充sku
        List<Sku> skuList = goodsClient.querySkus(spu.getId());
        //抽取需要数据,不需要全部只需要, id 图片第一张 价格 title
        List<Map> skuMapList=new ArrayList<>();
        skuList.forEach(sku -> {
            Map map = new HashMap();
            map.put("id",sku.getId());
            map.put("price",sku.getPrice());
            map.put("title",sku.getTitle());
            map.put("image", StringUtils.isEmpty(sku.getImages())?"":sku.getImages().split(",")[0]);
            skuMapList.add(map);

            //增加价格到集合
            priceList.add(sku.getPrice());

        });
        goods.setSkus(JsonUtils.serialize(skuMapList));

        //设置价格
        goods.setPrice(priceList);

        //将商品规格拼接
        //spec_group
        //spec_param:分页下的字段,商品在录入的时候,必须参照规格参数
        //规格name是统一的,规格值属于各个商品,值不一致
        //规格值：spu——detail:gen:通用属性， spe特有属性（多个 颜色 内存  内存）

//        goods.setSpecs();

        //取出spec 数据
//取出规格组的数据  根据cid 可用于搜索
        List<SpecParam> specParamList = specClient.querySpecParamsList(null, spu.getCid3(), true,null);
        //specParamList只有name没有value
        //取出规格值  spudetail中两个字段  拼接页面的搜索条件的值
        SpuDetail spuDetail = goodsClient.queryDetail(spu.getId());
        spuDetail.getGenericSpec();//通用字段  上市时间 品牌
        spuDetail.getSpecialSpec();//特有属性  颜色 内存大小
        //json字符串  不好匹配  json转对象
        Map<Long, String> genMap = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        Map<Long,List<String>> specMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
                });
        //有规格id  规格值

        //拼装esgoods中的规格
        Map<String, Object> goodsSpecMap = new HashMap<>();
        //拼凑出   name：value
        specParamList.forEach(specParam -> {
            specParam.getName();
            Object value = null;
            if (specParam.getGeneric()){  //是否是通用属性
                value = genMap.get(specParam.getId());

                //筛选该规格 是否是number类型 英寸(4-5,5-6)
                if (specParam.getNumeric()){
                    //如果是数值类型  在保存值时  加工  将值变成段  (0-4.0,4.0-5.0,5.0-5.5,5.5-6.0,6.0-)
                    //"5.2"---> 5.0-5.5
                    value = this.buildSegment(value.toString(),specParam);
                }

            }else {
                value = specMap.get(specParam.getId());
            }
            //装入新的集合中
            goodsSpecMap.put(specParam.getName(),value);

        });
        goods.setSpecs(goodsSpecMap);

        return goods;
    }


    private String buildSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

//    查询商品分页
    public PageResult<Goods> getGoodsPage(SearchPageBo searchPageBo) {
        // 创建查询构建器
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //如果有搜索的关键字就进行条件查询
        if (StringUtils.isNotEmpty(searchPageBo.getKey())){
            builder.withQuery(
                    QueryBuilders.boolQuery( ).must(
                        QueryBuilders.matchQuery("all",searchPageBo.getKey())));
        }
        //当前页. 需要修正 -1
        int page = searchPageBo.getPage();
        page=page>0? page-1:0;

        // 设置分页
        builder.withPageable(PageRequest.of(page,searchPageBo.getSize()));
        // 结果过滤
//        builder.withSourceFilter(new FetchSourceFilter(
//                new String[]{"id","skus","subTitle"},null));
        builder.withSourceFilter(new FetchSourceFilter(
                new String[]{"id","all","skus"},null));



        //处理规格 使用过滤不使用搜索,这样不会影响成绩   创建bool查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Map<String, String> filterMap = searchPageBo.getFilter();
        //如果页面使用过滤条件查询就拼接bool查询
        if (filterMap != null && filterMap.size() != 0){
            Set<String> filterSet = filterMap.keySet();
            //遍历map
            for (String key:filterSet){
                MatchQueryBuilder matchQueryBuilder=null;
                //如果是 分类与品牌查询的话, 直接设置查询条件即可
                if (key.equals("cid3") || key.equals("brandId")){
                    matchQueryBuilder = QueryBuilders.matchQuery(key,filterMap.get(key));
                }else {
                    //如果是规格条件查询的话  需要在字段上追加spec.xxx.keyword
                    matchQueryBuilder = QueryBuilders.matchQuery("specs."+key+".keyword",filterMap.get(key));
                }
                boolQueryBuilder.must(matchQueryBuilder);
            }
            builder.withFilter(boolQueryBuilder);
        }



        //查询结果
        Page<Goods> goodsPage = goodsRepository.search(builder.build());

        //判断高亮
        if (StringUtils.isNotEmpty(searchPageBo.getKey())){
            builder.withHighlightFields(new HighlightBuilder.Field("all")
                    .preTags("<font color='red'>").postTags("</font>"));
            Map<Long, String> hignMap = HignLightUtil.getHignLigntMap(elasticsearchTemplate,
                    builder.build(), Goods.class, "all");
            goodsPage.getContent().forEach(goods -> {
                goods.setAll(hignMap.get(goods.getId()));
            });
        }
        //总条数
        Long total = goodsPage.getTotalElements();
        //总页数 = 总条数/每页条数  向上取整
        Long totalPage = (long)Math.ceil(total.doubleValue()/searchPageBo.getSize());


        //填充聚合分类与品牌条件
        builder.addAggregation(AggregationBuilders.terms("cateGro").field("cid3"));
        builder.addAggregation(AggregationBuilders.terms("brandGro").field("brandId"));
        //转换聚合结果
        AggregatedPage<Goods> pages = (AggregatedPage<Goods>)goodsRepository.search(builder.build());

        //取出分类聚合结果
        LongTerms cateTerms = (LongTerms)pages.getAggregation("cateGro");
        //获得buckets 分组数据集
        List<LongTerms.Bucket> cateBuckets = cateTerms.getBuckets();

        //假设热度最高的cid=0 maxDocCount商品数量0
        final List<Long> maxDocCid=new ArrayList<>();
        maxDocCid.add(0l);
        final List<Long> maxDocCount=new ArrayList<>();
        maxDocCount.add(0l);

        //取出分类cid集合
        List<Long> cateIds = cateBuckets.stream().map(bucket -> {
            //如果商品数量 小于下一个商品数量
            if (maxDocCount.get(0)<bucket.getDocCount()){
                maxDocCount.set(0,bucket.getDocCount());
                maxDocCid.set(0,bucket.getKeyAsNumber().longValue());
            }

            return bucket.getKeyAsNumber().longValue();
        }).collect(Collectors.toList());
        
        //批量查询分类
        List<Category> categoryList = categoryClient.queryCateListByIds(cateIds);
        System.out.println("最终汇总的分类数据:"+categoryList.get(0).getName());

        //取出品牌聚合结果
        LongTerms brandTerms = (LongTerms)pages.getAggregation("brandGro");
        //获得buckets 分组数据
        List<LongTerms.Bucket> brandTermsBucket = brandTerms.getBuckets();
        //查询出品牌聚合List
        List<Brand> brandList = brandTermsBucket.stream().map(bucket -> {
            return brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
        }).collect(Collectors.toList());
        System.out.println("最终品牌的数据:"+brandList.size());

        //汇总规格筛选条件
        //  specMapList.add("屏幕尺寸":[5-6,6-7])
        //需要传  热度最高的cid
        System.out.println("商品数量最多 热度最高的分类是:"+maxDocCid.get(0));
        List<Map<String, Object>> specMapList = this.getSpecMapList(maxDocCid.get(0), searchPageBo);


        // 封装返回数据分页数据与聚合出的分类数据
        //return new PageResult<Goods>(goodsPage.getTotalElements(),totalPage,goodsPage.getContent());

        //                      总条数                     总页数
        return new SearchResult(goodsPage.getTotalElements(),totalPage,goodsPage.getContent(),categoryList,brandList,specMapList);
    }


    /**
     * 查询分类下的规格  es规格值
     * @return
     */
    public List<Map<String,Object>> getSpecMapList(Long cid,SearchPageBo bo){

        List<Map<String,Object>> specMapList=new ArrayList<>();
        

        //cid根据   查询哪个规格被筛选
        List<SpecParam> specParamList=specClient.querySpecParamsList(null,cid,true,null);

        NativeSearchQueryBuilder builder=new NativeSearchQueryBuilder();

        //设置查询条件
        if(StringUtils.isNotEmpty(bo.getKey())){
            builder.withQuery(QueryBuilders.boolQuery().must(
                    QueryBuilders.matchQuery("all",bo.getKey())
            )) ;
            //builder.withPageable(PageRequest.of(0,1));
        }


        //循环规格集合，得到规格名称， 根据规格名称做聚合
        //规格：操作系统
        //CPU品牌 CPU核数 CPU频率
        //内存  机身存储  主屏幕尺寸（英寸）  前置摄像头  后置摄像头 电池容量（mAh）
        specParamList.forEach(specParam -> {
            //规格name给你作为别名
            builder.addAggregation(AggregationBuilders.terms(specParam.getName()).field("specs."+specParam.getName()+".keyword"));
        });
        //查询得到聚合
        AggregatedPage<Goods> page= (AggregatedPage<Goods>) goodsRepository.search(builder.build());

        //循环规格名称 得到 规格名称聚合的数据
        specParamList.forEach(specParam -> {
            StringTerms specTerm= (StringTerms) page.getAggregation( specParam.getName());
            //取出单列，（规格值）【5-6，6-7】
            List<String> specValueList=specTerm.getBuckets().stream().map(bucket -> {
                return  bucket.getKeyAsString();
            }).collect(Collectors.toList());

            //将数据，放入集合
            Map<String,Object> specMap=new HashMap<>();
            specMap.put("key",specParam.getName());
            specMap.put("values",specValueList);
            specMapList.add(specMap);
        });


        return specMapList;
    }


    /**
     * 根据spuId新增 / 修改索引
     * @param  spuId
     *
     * */
    public void saveGoodForEs(Long spuId){
        Spu spu = goodsClient.querySpuById(spuId);
        Goods goods = this.buildGoodsBySpu(spu);
        this.goodsRepository.save(goods);
    }

    /**
     * 根据spuId删除索引
     * @param  spuId
     *
     * */
    public void deleteGoodForEs(Long spuId){
        this.goodsRepository.deleteById(spuId);
    }

}
