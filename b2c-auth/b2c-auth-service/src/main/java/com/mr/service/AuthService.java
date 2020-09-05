package com.mr.service;

import com.mr.bo.UserInfo;
import com.mr.client.UserClient;
import com.mr.config.JwtConfig;
import com.mr.pojo.User;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 根据用户密码账号  校验是否正确
     * */
    public String auth(String username,String password){
        try {
            //校验账号密码 通过fegin
            User user = userClient.query(username, password);
            if (user == null){
                //账号密码错误
                return null;
            }
            //账号密码正确
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());

            String token = JwtUtils.generateToken(userInfo, jwtConfig.getPrivateKey(),
                    jwtConfig.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
