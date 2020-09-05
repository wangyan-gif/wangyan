package com.mr.order.service;

import com.mr.order.mapper.VipOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VipOrderService {

    @Autowired
    private VipOrderMapper vipOrderMapper;


}
