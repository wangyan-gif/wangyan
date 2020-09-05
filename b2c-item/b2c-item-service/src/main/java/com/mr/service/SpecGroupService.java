package com.mr.service;


import com.mr.mapper.SpecGroupMapper;
import com.mr.service.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Component
public class SpecGroupService {

    @Autowired
    private SpecGroupMapper mapper;



    //查
    public List<SpecGroup> querySpecGroupByCid(Long cid){
        //定义查询条件
        Example example = new Example(SpecGroup.class);
        example.createCriteria().andEqualTo("cid",cid);
        //设置条件查询
        return mapper.selectByExample(example);
    }


    //增
    public int add(SpecGroup specGroup){
        //设置新增
        return mapper.insert(specGroup);
    }


    //改
    public int update(SpecGroup specGroup){
        //设置修改
        return mapper.updateByPrimaryKeySelective(specGroup);
    }

    //删
    public int delete(Long id){
        //设置修改
        return mapper.deleteByPrimaryKey(id);
    }


}
