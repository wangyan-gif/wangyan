package com.mr.service;

import com.mr.client.BrandClient;
import com.mr.client.CategoryClient;
import com.mr.client.GoodsClient;
import com.mr.client.SpecClient;
import com.mr.service.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;


    /*
    * 组装商品详情需要的数据
    *
    *
    *
    * */

    public Map<String,Object> getGoodInfo(Long spuId){
//        spu信息
        Spu spu = goodsClient.querySpuById(spuId);
//        spu分类(3级)
        List<Category> categoryList = categoryClient.queryCateListByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
//        spu品牌信息brand
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
//        spuDateil详情
        SpuDetail spuDetail = goodsClient.queryDetail(spuId);
//        规格组(分类id)
        List<SpecGroup> specGroupList = specClient.querySpecGroupList(spu.getCid3());
//        规格组填充规格参数
        specGroupList.forEach(specGroup -> {
            List<SpecParam> specParamList = specClient.querySpecParamsList(specGroup.getId(), null, null, null);
            specGroup.setSpecParamList(specParamList);
        });
//        sku 包含库存(stock)
        List<Sku> skuList = goodsClient.querySkus(spuId);

//        Map<Long,String> sku = new HashMap<>();
//        skuList.forEach(sku1 -> {
//            sku.put(sku1.getId(),sku1.getTitle());
//        });

//        特有规格参数(分类id)
        List<SpecParam> specParamsList = specClient.querySpecParamsList(null, spu.getCid3(), null, null);

        Map<Long,String> specParamsMap = new HashMap<>();
        specParamsList.forEach(specParam -> {
            specParamsMap.put(specParam.getId(),specParam.getName());
        });

//        参数map
        Map<String,Object> map = new HashMap<>();

        map.put("spu",spu);
        map.put("categoryList",categoryList);
        map.put("brand",brand);
        map.put("spuDetail",spuDetail);
        map.put("specGroupList",specGroupList);
        map.put("skuList",skuList);
        map.put("specParamsMap",specParamsMap);

        return map;
    }
}
