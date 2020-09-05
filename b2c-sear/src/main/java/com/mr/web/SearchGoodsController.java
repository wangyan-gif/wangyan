package com.mr.web;

import com.mr.common.utils.PageResult;
import com.mr.pojo.Goods;
import com.mr.service.GoodsIndexService;
import com.mr.service.bo.SearchPageBo;
import com.mr.service.pojo.Spu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("goods")
public class SearchGoodsController {

    @Autowired
    private GoodsIndexService goodsIndexService;

    //查询商品分类
    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> goodsPage(@RequestBody SearchPageBo  searchPageBo){
        PageResult<Goods> goodsPage = (PageResult<Goods>)goodsIndexService.getGoodsPage(searchPageBo);
        return ResponseEntity.ok(goodsPage);
    }

}
