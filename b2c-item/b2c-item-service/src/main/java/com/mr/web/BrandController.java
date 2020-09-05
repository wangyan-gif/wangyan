package com.mr.web;


import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.common.utils.PageResult;
import com.mr.service.BrandService;
import com.mr.service.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService service;

    @GetMapping("page")
    public ResponseEntity<PageResult> getBrandPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
    ){


        PageResult<Brand> pageResult = service.getBrandPage(page,rows,key,sortBy,desc);

        if (pageResult == null || pageResult.getItems().size() == 0){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(pageResult);
    }


    @PostMapping()
    public ResponseEntity<Void> add(Brand brand, @RequestParam(value ="cids") List<Long> cids){

        service.add(brand,cids);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<String> update(Brand brand, @RequestParam(value ="cids") List<Long> cids){

        service.update(brand,cids);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping()
    public ResponseEntity<String> delete(@RequestParam(value = "id") Long id){

        service.delete(id);

        if (id == null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok("删除成功");
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid){

        List<Brand> list = service.queryBrandByCid(cid);

        if (list == null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(list);
    }


    //根据品牌id查询品牌
    @GetMapping("queryBrandById")
    public ResponseEntity<Brand> queryBrandById(@RequestParam("id") Long id){

        return ResponseEntity.ok(service.queryBrandById(id));
    }

}
