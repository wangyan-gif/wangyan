package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.service.ItemService;
import com.mr.service.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("item")
public class ItemController {

    @Autowired
    private ItemService service;

    @PostMapping
    public ResponseEntity<Item> addItem(Item item){
        if(item.getPrice() == null){
            //不能为空
            //状态码  提示信息
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("价格参数不能为空");
            //throw new RuntimeException("价格参数不能为空");
            //throw new MrException(ExceptionEnums.PRICE_CANNOT_BE_NULL);
            throw new MrException(ExceptionEnums.NANME_TOO_LONG);
        }

        return ResponseEntity.status(HttpStatus.OK).body(service.saveItem(item));
    }

}
