package com.mr.service;

import com.mr.mapper.CategoryMapper;
import com.mr.service.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper mapper;

    public List<Category> queryCategoryByPid(Long pid){
        Category category = new Category();
        category.setParentId(pid);

        return mapper.select(category);
    }

    public void update(Category category){

        mapper.updateByPrimaryKeySelective(category);
    }

    public void add(Category category){
        mapper.insert(category);
    }

    public void delete(Long id){
        mapper.deleteByPrimaryKey(id);
    }

    public List<Category> queryByBrandId(Long bid) {
        return this.mapper.queryByBrandId(bid);
    }

    public List<String> queryNamesByIds(List<Long> ids){
        List<Category> list = this.mapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        for (Category category:list){
            names.add(category.getName());
        }
        return names;
    }

    public List<Category> queryCateListByIds(List<Long> ids){

        return mapper.selectByIdList(ids);
    }
}
