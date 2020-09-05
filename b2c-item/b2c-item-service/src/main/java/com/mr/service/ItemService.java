package com.mr.service;

import com.mr.service.pojo.Item;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ItemService {

    public Item saveItem(Item item){
        item.setId(new Random().nextInt());
        return item;
    }

}
