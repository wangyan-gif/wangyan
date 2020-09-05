package com.mr.service.api;

import com.mr.common.utils.PageResult;
import com.mr.service.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("page")
    public PageResult getBrandPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
    );


    @PostMapping()
    public Void add(Brand brand, @RequestParam(value ="cids") List<Long> cids);

    @PutMapping()
    public String update(Brand brand, @RequestParam(value ="cids") List<Long> cids);

    @DeleteMapping()
    public String delete(@RequestParam(value = "id") Long id);

    @GetMapping("cid/{cid}")
    public List<Brand> queryBrandByCid(@PathVariable("cid") Long cid);

    @GetMapping("queryBrandById")
    public Brand queryBrandById(@RequestParam("id") Long id);

}
