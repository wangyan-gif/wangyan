package com.mr.web;

import com.mr.bo.UserInfo;
import com.mr.common.utils.CookieUtils;
import com.mr.config.JwtConfig;
import com.mr.service.AuthService;
import com.mr.util.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 登陆
     * */
    @PostMapping("login")
    public ResponseEntity<Void> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response){

        System.out.println("request 接受参数: "+request.getParameter("username"));
        System.out.println("request 访问者url: "+request.getRequestURI());
        System.out.println("request 访问者url: "+request.getHeader("host"));

        //1:校验账号密码
        String token = authService.auth(username,password);

        //3:失败 401
        if (StringUtils.isEmpty(token)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //2:成功 生成token 保存用户到浏览器中的cookie中
        System.out.println("token:"+token);
        CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),
                token,jwtConfig.getCookieMaxAge(),true);

        return ResponseEntity.ok(null);
    }

    /**
     * 所有页面都会调用此请求
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("B2C_TOKEN") String token,
            HttpServletRequest request,
            HttpServletResponse response){
        System.out.println("获取的cookie的值:"+token);

        try {
            //解析token 成功返回用户数据  失败 篡改 过期 403
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());


            //生成token
            String newToken = JwtUtils.generateToken(user, jwtConfig.getPrivateKey(),
                    jwtConfig.getExpire());
            //刷新剩余时间
            CookieUtils.setCookie(request,response,jwtConfig.getCookieName(),
                    newToken,jwtConfig.getCookieMaxAge(),true);


            //返回用户数据
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            //无权限
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }

}
