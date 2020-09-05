package com.mr.utils;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Goods;
import com.mr.service.pojo.Brand;
import com.mr.service.pojo.Category;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class SearchResult extends PageResult<Goods> {

    private List<Category> categoryList;

    private List<Brand> brandList;

    private List<Map<String,Object>> specMap; // 规格参数过滤条件

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categoryList, List<Brand> brandList
            ,List<Map<String,Object>> specMap
    ) {
        super(total, totalPage, items);
        this.categoryList = categoryList;
        this.brandList = brandList;
        this.specMap = specMap;
    }
}