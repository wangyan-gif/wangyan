package com.mr.util;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MqMessage {

    @Autowired
    private AmqpTemplate amqpTemplate;

    //发送消息工具类,指定交换机,routingkey 参数
    public void sendMessage(String exchangeName,String routIngKey,Object message){
        this.amqpTemplate.convertAndSend(exchangeName,routIngKey,message);
    }

}
