package com.mr.service;

import com.mr.mapper.UserMapper;
import com.mr.pojo.User;
import com.mr.util.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;

    //检查用户名 或者 手机号 是否可用
    public Boolean checkPhoneOrName(String data,Integer type){

        User user = new User();
        if(type == 1){
            user.setUsername(data);
        }else if(type == 2){
            user.setPhone(data);
        }

        //返回到查询数据  0 不存在  1 存在
        return mapper.selectCount(user) == 0;
    }

    public boolean register(User user) {
        //设置数据时间
        user.setCreated(new Date());
        //设置uuid 盐
        user.setSalt(Md5Utils.generateSalt());
        //将原始密码进行加密,配合盐进行加密,防止破解
        user.setPassword(Md5Utils.md5Hex(user.getPassword(),user.getSalt()));
        //返回结果为1 成功  0 失败
        return mapper.insert(user) == 1;


    }

    public User query(String username, String password) {
        User us = new User();
        us.setUsername(username);
        User user = mapper.selectOne(us);
        //根据用户名查询用户,校验用户名,密码是否正确
        if (user==null){
            return null;
            //原始密码加密加盐后,与数据库密码对比
        }else if (!user.getPassword().equals(Md5Utils.md5Hex(password,user.getSalt()))){
            return null;
        }else {
            return user;
        }
    }
}
