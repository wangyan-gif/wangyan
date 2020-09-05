package com.test;

import com.mr.SearApplication;
import com.mr.client.BrandClient;
import com.mr.client.GoodsClient;
import com.mr.common.utils.PageResult;
import com.mr.dao.GoodsRepository;
import com.mr.pojo.Goods;
import com.mr.service.GoodsIndexService;
import com.mr.service.pojo.SpuBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SearApplication.class })
public class TestClilent {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsIndexService goodsIndexService;

    @Test
    public void testGoodClient(){
        //分批导入
        int page = 0;
        boolean load = true;
        int count = 0;
        while (load){
            List<Goods> goodsList = new ArrayList<>();
            PageResult<SpuBo> pageResult = goodsClient.querySpuByPage(page++,10,true,"");
            pageResult.getItems().forEach(spuBo -> {
                //System.out.println(spuBo.getTitle()+" "+spuBo.getBrandName());
                //加工spu数据
                goodsList.add(goodsIndexService.buildGoodsBySpu(spuBo));
            });
            //System.out.println("spu商品ok");

            goodsRepository.saveAll(goodsList);
            count += 10;
            //最后一页结束循环
            if (pageResult.getItems().size()<10){
                load=false;
            }
        }
        System.out.println("一共增加了: "+count+"数据");
    }

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Test
    public void createGoodIndex(){
        elasticsearchTemplate.createIndex(Goods.class);

        elasticsearchTemplate.putMapping(Goods.class);
    }


    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void sendMessage() throws InterruptedException {
        String message = " good good study ";

        amqpTemplate.convertAndSend("item-spu","save",message);

        System.out.println("发送成功: OK");

        Thread.sleep(1000);
    }

}
