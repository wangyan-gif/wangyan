package com.mr.service;

import com.mr.bo.UserInfo;
import com.mr.client.GoodClient;
import com.mr.common.utils.JsonUtils;
import com.mr.pojo.Cart;
import com.mr.service.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private GoodClient goodClient;

    static final String KEY_PREFIX = "b2c:cart:uid:";

    /**
     * 增加购物车商品到redis
     * @param cart
     * @param userInfo
     */
    public void addCart(Cart cart, UserInfo userInfo) {
        System.out.println("skuId:"+cart.getSkuId());
        // Redis的key增加用户id
        String key = KEY_PREFIX + userInfo.getId();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        //是否购买过
        Boolean aBoolean = hashOps.hasKey(skuId.toString());
        if (aBoolean){
            // 存在证明购买过，要进行数量新增
            String json = hashOps.get(skuId.toString()).toString();
            cart  = JsonUtils.parse(json, Cart.class);
            // 修改购物车数量
            cart.setNum(cart.getNum()+num);
        }else {
            // 不存在，证明没有购买过 就新增sku信息
            cart.setUserId(userInfo.getId());
            // 前台只传了skuid和购买数量，其他数据要进行查询
            Sku sku = goodClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" :
                    StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        // 将购物车数据写入redis中到hash类型
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车数据
     * @param userInfo
     * @return
     */
    public List<Cart> queryCartList(UserInfo userInfo) {
        // 判断用户是否有购物车数据
        String key = KEY_PREFIX + userInfo.getId();
        //如果不存在就直接返回
        if (!this.redisTemplate.hasKey(key)){
            return null;
        }
        //获得绑定key的hash对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> cartList = hashOps.values();
        // 判断是否有数据
        if (CollectionUtils.isEmpty(cartList)){
            return  null;
        }
        // 查询购物车数据
        return cartList.stream().map(cart ->
            JsonUtils.parse(cart.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 修改购物车内商品数量
     */
    public void updateNum(Long skuId, Integer num, UserInfo userInfo) {

        //拼接rediskey
        String key = KEY_PREFIX + userInfo.getId();
        //获得绑定key的hash对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 获取购物车某个具体商品数据
        String json = hashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);
        //覆盖商品购买数量
        cart.setNum(num);
        // 写入购物车
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    /**
     * 删除购物车数据
     */
    public void deleteCart(String skuId, UserInfo userInfo) {
        //获得当前用户的购物车 hash对象
        String key = KEY_PREFIX + userInfo.getId();
        System.out.println("skuId 值: "+userInfo.getId());
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        //hash对象中删除sku信息
        hashOps.delete(skuId);
    }
}
