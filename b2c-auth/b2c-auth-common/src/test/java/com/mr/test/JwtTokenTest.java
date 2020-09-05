package com.mr.test;

import com.mr.util.JwtUtils;
import com.mr.util.RsaUtils;
import com.mr.bo.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTokenTest {

    //公钥位置
    private static final String pubKeyPath = "E:/1908/5month/oracle/reskey/rea.pub";
    //私钥位置
    private static final String priKeyPath = "E:/1908/5month/oracle/reskey/rea.pri";
    //公钥对象
    private PublicKey publicKey;
    //私钥对象
    private PrivateKey privateKey;


    /**
     * 生成公钥私钥 根据密文
     * @throws Exception
     */
    @Test
    public void genRsaKey() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "9527");
    }


    /**
     * 从文件中读取公钥私钥
     * @throws Exception
     */
    @Before
    public void getKeyByRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    /**
     * 根据用户信息结合私钥生成token
     * @throws Exception
     */
    @Test
    public void genToken() throws Exception {
        // 生成token
        UserInfo userInfo = new UserInfo(1l, "tomcat");
        String token = JwtUtils.generateToken(userInfo, privateKey, 2);
        System.out.println("user-token = " + token);
    }


    /**
     * 结合公钥解析token
     * @throws Exception
     */
    @Test
    public void parseToken() throws Exception {
//        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0b21jYXQiLCJleHAiOjE1OTg0MjQ0MjB9.W7LxS5R8ER3FP-AuNifqFgg1dJG-ZqjVP_FuVMyX1xwCetv7Vse-OIVu6oRE2a0lsV-vTEr9Vod-IepI4jrsnifC09KtUi6cm4IdMBdgdujYFiSrzTMhnhaIC7iepWaLm4Lhi_yToqgXOkUk1LArce3sHsmiYEjMDx1iFi5PX6Y";
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJ0b21jYXQiLCJleHAiOjE1OTg0NDkwMjh9.B8Pf0QjIRY3R6Uq3kS4GzqjjeqXhIslGAoaCZzar6-BsCy64YmQnQXarQx_xHwHCFLRO1DhtEUcrIlt-GtKdFAyc2tZYV4fFebESM08_V8wk4Wo89t9344cvXNs2lY0WRdrGMDLUbWsKC1naHvfUW_1nrxULImfVfuny0TPMgQo";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}


