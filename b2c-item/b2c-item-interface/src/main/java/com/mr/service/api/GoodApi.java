package com.mr.service.api;

import com.mr.common.utils.PageResult;
import com.mr.service.pojo.Sku;
import com.mr.service.pojo.Spu;
import com.mr.service.pojo.SpuBo;
import com.mr.service.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("goods")
public interface GoodApi {

    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "key",required = false) String key);

    @PostMapping
    public Void saveGoods(@RequestBody SpuBo spuBo);

    @GetMapping("/spu/detail/{spuId}")
    public SpuDetail queryDetail(@PathVariable("spuId") Long spuId);

    @GetMapping("/skuList/{spuId}")
    public List<Sku> querySkus(@PathVariable("spuId") Long spuId);

    @PutMapping
    public Void update(@RequestBody SpuBo spuBo);

    @DeleteMapping
    public Void delGoods(@RequestParam("id") Long id);

    //根据spuId查询spu
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long spuId);
    //根据skuId查询spu
    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id") Long skuId);
}
