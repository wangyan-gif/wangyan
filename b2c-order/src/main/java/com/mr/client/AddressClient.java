package com.mr.client;

import com.mr.order.bo.AddressBo;

//地址client,没有做地址服务,这是模拟的client方法
public class AddressClient {
    /**
     * 根据地址id查询详细地址
     **/
    public static AddressBo getAddressById(Long id){
        return AddressBo.addreddMap.get(id);
    }
}
