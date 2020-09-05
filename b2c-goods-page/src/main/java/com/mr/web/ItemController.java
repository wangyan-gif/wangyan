package com.mr.web;

import com.mr.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("{id}.html")
    public String toGoodInfo(@PathVariable("id") Long spuId, ModelMap map){
       map.put("key","value");

//       根据id查询各种数据
       Map<String, Object> goodMap = itemService.getGoodInfo(spuId);
       map.putAll(goodMap);

       System.out.println("商品详情:" + spuId);
       return "item";
    }

}
