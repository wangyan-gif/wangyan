package com.mr.service;

import com.mr.mapper.SpecParamMapper;
import com.mr.service.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpecParamService {

    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecParam> querySpecParamsList(Long gid,Long cid,Boolean searching,Boolean generic) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        param.setCid(cid);
        param.setSearching(searching);
        param.setGeneric(generic);
        return this.paramMapper.select(param);
    }

    public int addParam(SpecParam specParam) {
        return this.paramMapper.insertSelective(specParam);
    }

    public int updateParam(SpecParam specParam) {
        return paramMapper.updateByPrimaryKeySelective(specParam);
    }

    public int deleteParam(Long id) {
        return paramMapper.deleteByPrimaryKey(id);
    }
}
