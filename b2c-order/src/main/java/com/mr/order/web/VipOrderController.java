package com.mr.order.web;

import com.mr.common.utils.CookieUtils;
import com.mr.config.JwtConfig;
import com.mr.order.pojo.Vip;
import com.mr.order.service.VipOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class VipOrderController {
    @Autowired
    private VipOrderService vipOrderService;
    @Autowired
    private JwtConfig jwtConfig;

    //接受的参数 status 状态
    // 月份 数字/字符串
    // username

    @PostMapping("vipRecharge")
    public ResponseEntity<Long> vipRecharge(@RequestBody Vip vip, HttpServletRequest request){
        String token = CookieUtils.getCookieValue(request, jwtConfig.getCookieName());



        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
