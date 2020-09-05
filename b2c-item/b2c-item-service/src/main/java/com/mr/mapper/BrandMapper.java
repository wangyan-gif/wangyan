package com.mr.mapper;

import com.mr.service.pojo.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BrandMapper extends tk.mybatis.mapper.common.Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("delete from tb_category_brand where brand_id=#{bid}")
    int delBrandCateByBid(@Param("bid") Long bid);

    @Select("SELECT * from tb_brand where id in (SELECT brand_id FROM tb_category_brand where category_id = #{cid})")
    List<Brand> queryBrandByCid(@Param("cid") Long cid);
}
