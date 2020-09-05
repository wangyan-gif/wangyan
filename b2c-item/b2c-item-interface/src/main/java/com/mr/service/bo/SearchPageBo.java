package com.mr.service.bo;

import lombok.Data;

import java.util.Map;

@Data
public class SearchPageBo {

    private String key; //搜索条件
    private Integer page=0; //默认第0页
    private Integer size=10; //每页10条数据
    private Map<String,String> filter; //筛选条件

}
