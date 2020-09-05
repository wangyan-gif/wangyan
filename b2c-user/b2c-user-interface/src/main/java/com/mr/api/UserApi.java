package com.mr.api;

import com.mr.pojo.User;
import org.springframework.web.bind.annotation.*;

public interface UserApi {

    /**
     * 校验数据是否可用
     * */
    @GetMapping("check/{data}/{type}")
    public Boolean check(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type);

    /**
     * 注册用户
     *
     */
    @PostMapping("register")
    public Void register(User user);

    /**
     * 根据用户名和密码查询用户
     *
     */
    @GetMapping("query")
    public User query(
            @RequestParam("username") String username,
            @RequestParam("password") String password);

}
