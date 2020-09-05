package com.mr.mapper;

import com.mr.service.pojo.Sku;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;

@Mapper
public interface SkuMapper extends tk.mybatis.mapper.common.Mapper<Sku> , DeleteByIdListMapper<Sku,Long> {
}
