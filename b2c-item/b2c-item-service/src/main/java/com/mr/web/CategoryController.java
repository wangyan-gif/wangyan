package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.service.CategoryService;
import com.mr.service.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("list")
    public ResponseEntity<List<Category>> getCateList(
            @RequestParam(value = "pid",defaultValue = "0") Long pid){

        List<Category> list = service.queryCategoryByPid(pid);

        if (list == null && list.size() == 0){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok(list);
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        List<Category> list = this.service.queryByBrandId(bid);
        System.out.println("bid值为:"+bid);
        if (list == null || list.size() < 1) {
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("add")
    public ResponseEntity<String> add(@RequestBody Category category){

        service.add(category);

        if (category == null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok("新增成功");
    }

    @PutMapping("update")
    public ResponseEntity<String> update(@RequestBody Category category){

        if (category == null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        service.update(category);
        return ResponseEntity.ok("修改成功");
    }

    @DeleteMapping("delete")
    public ResponseEntity delete(@RequestParam(value = "id") Long id){

        service.delete(id);

        if (id == null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        return ResponseEntity.ok("删除成功");
    }


    //根据分类id查询多个分类
    @GetMapping("queryCateListByIds")
    public ResponseEntity<List<Category>> queryCateListByIds(@RequestParam("ids") List<Long> ids){

        return ResponseEntity.ok(service.queryCateListByIds(ids));
    }

}
