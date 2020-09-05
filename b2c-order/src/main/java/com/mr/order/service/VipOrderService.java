package com.mr.order.service;

import com.mr.bo.UserInfo;
import com.mr.order.mapper.VipOrderMapper;
import com.mr.order.pojo.Vip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VipOrderService {

    @Autowired
    private VipOrderMapper vipOrderMapper;

    @Transactional
    public Long vipRecharge(Vip vip, UserInfo userInfo) {


        return vip.getUserId();
    }
}
