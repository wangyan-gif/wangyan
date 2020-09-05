package com.mr.web;

import com.mr.bo.UserInfo;
import com.mr.common.utils.CookieUtils;
import com.mr.config.JwtConfig;
import com.mr.pojo.Cart;
import com.mr.service.CartService;
import com.mr.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService service;
    @Autowired
    private JwtConfig jwtConfig;

    /**
     * 查询购物车数据
     */
    @GetMapping("queryCartList")
    public ResponseEntity<List<Cart>> queryCartList(
            @CookieValue("B2C_TOKEN") String token){


        try {
            //获得登陆用户数据
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //根据用户查询购物车数据
            List<Cart> cartList = this.service.queryCartList(userInfo);
            //如果无购物车数据则返回无数据状态
            if (cartList == null){
                return new  ResponseEntity(HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(cartList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    /**
     * 添加购物车
     */
    @PostMapping("addCart")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart,
                                        @CookieValue("B2C_TOKEN") String token){
        try {
            //获得登录用户数据
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            System.out.println(userInfo.getId()+" 购买"+cart.getSkuId());
            //增加购物车到缓存
            this.service.addCart(cart,userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 修改购物车内商品数量
     */
    @PutMapping("updateNum")
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId,
                                          @RequestParam("num") Integer num,
                                          @CookieValue("B2C_TOKEN") String token){

        try {
            //获得登陆用户数据
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //修改购买数量
            this.service.updateNum(skuId,num,userInfo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * 删除购物车数据
     */
    @DeleteMapping("deleteCart/{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId,
                                           @CookieValue("B2C_TOKEN") String token){

        try {
            System.out.println("skuId值1: "+skuId);
            //获得登陆用户数据
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtConfig.getPublicKey());
            //删除购物车内某条数sku数据
            this.service.deleteCart(skuId,userInfo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
