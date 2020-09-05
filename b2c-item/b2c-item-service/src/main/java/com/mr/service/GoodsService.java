package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.common.utils.PageResult;
import com.mr.mapper.*;
import com.mr.service.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<SpuBo> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {

        // 1、查询SPU
        // 分页,最多允许查200条
        PageHelper.startPage(page,Math.min(rows,200));

        // 创建查询条件
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 是否过滤上下架
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        // 是否模糊查询
        if (!StringUtils.isBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        Page<Spu> pageInfo = (Page<Spu>) this.spuMapper.selectByExample(example);
        List<SpuBo> list = pageInfo.getResult().stream().map(spu -> {
            // 把spu变为 spuBo
            SpuBo spuBo = new SpuBo();
            // 属性拷贝
            BeanUtils.copyProperties(spu,spuBo);

            // 2、查询spu的商品分类名称,要查三级分类
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            // 将分类名称拼接后存入
            spuBo.setCategoryName(StringUtils.join(names,"/"));

            // 3、查询spu的品牌名称
//            Stream<String> streamNames = names.stream().map(name -> {
//                return name.getName();
//            });
//            spuBo.setBrandName(streamNames);

            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBrandName(brand.getName());


            return spuBo;
        }).collect(Collectors.toList());
        return new PageResult<>(pageInfo.getTotal(),list);
    }

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Transactional//事务  全成功或全失败
    public void save(SpuBo spuBo) {
        //当前时间
        Date now = new Date();

        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo,spu);

        //保存spu
        spu.setCreateTime(now);
        spu.setLastUpdateTime(now);
        spu.setValid(true);
        spu.setSaleable(true);
        spuMapper.insert(spu);
        //保存spu详情
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);

        //保存sku
        List<Sku> skuList = spuBo.getSkus();

        //生产者发送消息
        amqpTemplate.convertAndSend("item-spu","item.save",spu.getId());

        System.out.println("spuId : " + spu.getId());

        this.saveSkuAndStock(skuList,spu.getId());


//        skuList.forEach(sku -> {
//            if (sku.getEnable()){ //是否有效
//                sku.setLastUpdateTime(now);
//                sku.setCreateTime(now);
//                sku.setSpuId(spu.getId());
//                skuMapper.insert(sku);
//
//                //保存stock
//                Stock stock = new Stock();
//                stock.setSkuId(sku.getId());
//                stock.setStock(sku.getStock());
//                stockMapper.insert(stock);
//            }
//        });
    }

    //删除
    @Transactional
    public void delete(Long id) {
        //通过主键id删除spu
        Spu spu = new Spu();
        spu.setId(id);
        spuMapper.deleteByPrimaryKey(spu.getId());
        //通过主键id删除spuDetail
        SpuDetail spuDetail = new SpuDetail();
        spuDetail.setSpuId(id);
        spuDetailMapper.deleteByPrimaryKey(spuDetail.getSpuId());


        Sku sku = new Sku();
        sku.setSpuId(id);

        //查询出sku(没有库存信息)
        List<Sku> skuList = skuMapper.select(sku);
        //获取到所有skuId
        List<Long> skuIds = skuList.stream().map(s -> s.getId()).collect(Collectors.toList());

        //通过删除stock
        stockMapper.deleteByIdList(skuIds);

        //通过对象(spuId)删除sku
        skuMapper.delete(sku);






    }

    //返回spu
    public SpuDetail querySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }
    //返回sku
    public List<Sku> querySkuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        //查询出sku(没有库存信息)
        List<Sku> skus = skuMapper.select(sku);
        //给sku赋值库存信息
        skus.forEach(skuFe -> {
            //获得skuid查询库存信息表,取出stock赋值给sku对的stock字段
            Stock stock = stockMapper.selectByPrimaryKey(skuFe.getId());
            skuFe.setStock(stock.getStock());
        });
        System.out.println();
        return skus;
    }

    //修改
    @Transactional
    public void update(SpuBo spuBo) {

        //修改spu
        Spu spu = new Spu();
        BeanUtils.copyProperties(spuBo,spu);

        //最终修改时间
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);//添加时间
        spu.setValid(null);//是否有效
        spu.setSaleable(null);//是否上架
        this.spuMapper.updateByPrimaryKeySelective(spu);

        //修改detail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        this.spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        //库存delete ---> insert
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询出sku(没有库存信息)
        List<Sku> skuList = skuMapper.select(sku);

        //获取到所有skuId
        List<Long> skuIds = skuList.stream().map(s -> s.getId()).collect(Collectors.toList());

        //删除库存表信息
        stockMapper.deleteByIdList(skuIds);
        //删除详情表信息
        skuMapper.deleteByIdList(skuIds);

        //保存sku
        this.saveSkuAndStock(spuBo.getSkus(),spu.getId());


//        List<Sku> skuList1 = spuBo.getSkus();
//        skuList1.forEach(sku1 -> {
//            if (sku1.getEnable()){ //是否有效
//                sku1.setLastUpdateTime(new Date());
//                sku1.setCreateTime(new Date());
//                sku1.setSpuId(spu.getId());
//                skuMapper.insert(sku1);
//
//                //保存stock
//                Stock stock = new Stock();
//                stock.setSkuId(sku1.getId());
//                stock.setStock(sku1.getStock());
//                stockMapper.insert(stock);
//            }
//        });
    }

    //新增SkuAndStock
    public void saveSkuAndStock(List<Sku> skuList,Long spuId){
        skuList.forEach(sku -> {
                sku.setLastUpdateTime(new Date());
                sku.setCreateTime(new Date());
                sku.setSpuId(spuId);
                skuMapper.insert(sku);

                //保存stock
                Stock stock = new Stock();
                stock.setSkuId(sku.getId());
                stock.setStock(sku.getStock());
                stockMapper.insert(stock);
        });
    }

    public void saleable(Boolean saleable) {

        if (saleable == true){

        }else {

        }

    }

    public Spu querySpuById(Long id){
        return spuMapper.selectByPrimaryKey(id);
    }

    public Sku querySkuById(Long skuId) { return skuMapper.selectByPrimaryKey(skuId);}
}
