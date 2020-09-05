package com.mr.web;

import com.mr.pojo.User;
import com.mr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    /**
     * 校验数据是否可用
     * */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> check(
            @PathVariable(value = "data") String data,
            @PathVariable(value = "type") Integer type){

        //参数有误  400
        if(data == null || type == null || type > 2){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Boolean result = null;
        try {
            //查询结果返回数据
            result = service.checkPhoneOrName(data, type);
        }catch (Exception e){
            //服务器内部异常  500
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 注册用户
     *
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(User user){
        boolean resu = service.register(user);

        //注册失败  400
        if (!resu){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * 根据用户名和密码查询用户
     *
     */
    @GetMapping("query")
    public ResponseEntity<User> query(
            @RequestParam("username") String username,
            @RequestParam("password") String password){
        User user = service.query(username,password);

        //查询结果为null返回400
        if (user == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(user);
    }

}
