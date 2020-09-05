package com.mr.service.api;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.common.utils.PageResult;
import com.mr.service.pojo.Category;
import com.mr.service.pojo.Sku;
import com.mr.service.pojo.SpuBo;
import com.mr.service.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    @GetMapping("list")
    public List<Category> getCateList(
            @RequestParam(value = "pid",defaultValue = "0") Long pid);

    @GetMapping("bid/{bid}")
    public List<Category> queryByBrandId(@PathVariable("bid") Long bid);

    @PostMapping("add")
    public String add(@RequestBody Category category);

    @PutMapping("update")
    public String update(@RequestBody Category category);

    @DeleteMapping("delete")
    public void delete(@RequestParam(value = "id") Long id);

    //根据分类id查询多个分类
    @GetMapping("queryCateListByIds")
    public List<Category> queryCateListByIds(@RequestParam("ids") List<Long> ids);


}
