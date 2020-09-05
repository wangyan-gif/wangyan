package com.mr.consumer;

import com.mr.client.GoodsClient;
import com.mr.dao.GoodsRepository;
import com.mr.pojo.Goods;
import com.mr.service.GoodsIndexService;
import com.mr.service.pojo.Spu;
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
public class SpuLinstener {

    @Autowired
    private GoodsIndexService service;
//    @Autowired
//    private GoodsRepository goodsRepository;
//    @Autowired
//    private GoodsClient goodsClient;
//
//    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item_spu_queue_1",durable = "true"),
//    exchange = @Exchange(value = "item-spu",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
//    key = {"item.save"}))
//    public void listen(Long id){
//        System.out.println("消费者1接收到消息:" + id);
//        Spu spu = goodsClient.querySpuById(id);
//        //System.out.println("spu : "+spu);
//        Goods goods = service.buildGoodsBySpu(spu);
//        //System.out.println("goods : "+goods);
//        goodsRepository.save(goods);
//    }

    /**
     * 新增 / 修改spu 修改es库
     * 创建队列,绑定交换机  指定类型 ,持久化fouting等参数
     *
     * */
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item_spu_queue_1",durable = "true"),
    exchange = @Exchange(value = "item-spu",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
    key = {"item.save"}))
    public void saveEs(String id){

    }

}
