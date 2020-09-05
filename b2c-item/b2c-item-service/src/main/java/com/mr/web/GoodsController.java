package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.common.utils.PageResult;
import com.mr.service.GoodsService;
import com.mr.service.pojo.Sku;
import com.mr.service.pojo.Spu;
import com.mr.service.pojo.SpuBo;
import com.mr.service.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("goods")
public class GoodsController {

    @Autowired
    private GoodsService service;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "key",required = false) String key){


        PageResult<SpuBo> result = this.service.querySpuByPage(page,rows,saleable,key);
        if (result == null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){

        try {
            this.service.save(spuBo);
        }catch (Exception e){
            e.printStackTrace();
            throw new MrException(ExceptionEnums.ADD_GOODS_NULL);
        }
        return  ResponseEntity.ok(null);

    }

    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> queryDetail(@PathVariable("spuId") Long spuId){

        return ResponseEntity.ok(this.service.querySpuId(spuId));
    }

    @GetMapping("/skuList/{spuId}")
    public ResponseEntity<List<Sku>> querySkus(@PathVariable("spuId") Long spuId){
        List<Sku> skus = this.service.querySkuId(spuId);
        return ResponseEntity.ok(skus);
    }

    //根据spuId查询spu
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long spuId){

        return ResponseEntity.ok(this.service.querySpuById(spuId));

    }
    //根据skuId查询spu
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id") Long skuId){

        return ResponseEntity.ok(this.service.querySkuById(skuId));

    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody SpuBo spuBo){

        try {
            this.service.update(spuBo);
        }catch (Exception e){
            e.printStackTrace();
            throw new MrException(ExceptionEnums.UPDATE_GOODS_NULL);
        }

        return  ResponseEntity.ok(null);
    }

//    @PutMapping()
//    public ResponseEntity<Void> saleable(@RequestBody Boolean saleable){
//
//        service.saleable(saleable);
//
//        return ResponseEntity.ok(null);
//    }


    @DeleteMapping
    public ResponseEntity<Void> delGoods(@RequestParam("id") Long id){

        System.out.println(id);

        this.service.delete(id);

        if (id == null){
            throw new MrException(ExceptionEnums.DEL_GOODS_NULL);
        }

        return ResponseEntity.ok(null);
    }



}
