package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.common.utils.PageResult;
import com.mr.mapper.BrandMapper;
import com.mr.service.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BrandService {

    @Autowired
    private BrandMapper mapper;


    public PageResult<Brand> getBrandPage(Integer page, Integer rows, String key, String sortBy, Boolean desc){

        PageHelper.startPage(page,rows);

        Example example = new Example(Brand.class);

        if (key != null && !key.equals("")){
            example.createCriteria().andLike("name","%"+key.trim()+"%");
        }

        if (sortBy != null && !sortBy.equals("")){
            example.setOrderByClause(sortBy+ (desc?" asc":" desc"));
        }

        Page<Brand> pageInfo =(Page<Brand>) mapper.selectByExample(example);

        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getResult());
    }

    public void add(Brand brand, List<Long> cids){

        this.mapper.insertSelective(brand);

        for (Long cid : cids){
            this.mapper.insertCategoryBrand(cid, brand.getId());
            System.out.println("cid:"+cid);
        }
    }

    @Transactional
    public void update(Brand brand, List<Long> cids){
        mapper.updateByPrimaryKey(brand);

        //删除原来的
        mapper.delBrandCateByBid(brand.getId());

        for (Long cid : cids){
            this.mapper.insertCategoryBrand(cid, brand.getId());
            System.out.println("cid:"+cid);
        }
    }

    public void delete(Long id) {
        mapper.deleteByPrimaryKey(id);
    }

    public List<Brand> queryBrandByCid(Long cid) {
        return mapper.queryBrandByCid(cid);
    }

    public Brand queryBrandById(Long id) {
        return mapper.selectByPrimaryKey(id);
    }
}
