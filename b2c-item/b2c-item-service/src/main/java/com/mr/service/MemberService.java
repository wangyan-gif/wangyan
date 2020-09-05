package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.common.utils.PageResult;
import com.mr.mapper.MemberMapper;
import com.mr.service.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class MemberService {

    @Autowired
    private MemberMapper mapper;

    public PageResult<User> MemberList(Integer page, Integer rows, String key, String sortBy, Boolean desc) {

        PageHelper.startPage(page,rows);

        Example example = new Example(User.class);

        if (key != null && !key.equals("")){
            example.createCriteria().andLike("name","%"+key.trim()+"%");
        }

        System.out.println(3333);

        if (sortBy != null && !sortBy.equals("")){
            example.setOrderByClause(sortBy+ (desc?" asc":" desc"));
        }

        Page<User> pageInfo =(Page<User>) mapper.selectByExample(example);

        return new PageResult<User>(pageInfo.getTotal(),pageInfo.getResult());
    }
}
