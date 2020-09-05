package com.mr.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {


    /**
     * 登陆方法
     * */
    @GetMapping("login")
    public String login (String username , String password, HttpSession session){
        if (username.equals("hanxi") && password.equals("hanxi")){
            System.out.println("账号密码正确,登陆成功");
            session.setAttribute("admin_user",username);
            return "login ok";
        }else {
            return "login error  username or password is error";
        }
    }

    /**
     * 模拟订单
     * */
    @GetMapping("showOrder")
    public String showOrder(HttpSession session){
        if (session.getAttribute("admin_user")!=null){
            System.out.println("订单的sessionId"+session.getId());
            return "这是 "+session.getAttribute("admin_user")+" 订单页面";
        }else {
            return "无用户登陆 "+session.getAttribute("admin_user")+" 无数据";
        }

    }


}
