package com.mr.service.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SpuBo extends Spu{

    private String categoryName;// 商品分类名称

    private String brandName;// 品牌名称

    SpuDetail spuDetail;// 商品详情

    List<Sku> skus;// sku列表

}
