package com.mr.consumer;

import com.mr.service.FileStaticService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mq队列监听
 * */
@Component
public class EsLinstener {

    @Autowired
    private FileStaticService service;

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item_spu_queue_2",durable = "true"),
    exchange = @Exchange(value = "item-spu",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
    key = {"item.#"}))
    public void listen(Long id) throws Exception {
        System.out.println("消费者2接收到消息:" + id);
        service.createStaticHtml(id);

    }


}
