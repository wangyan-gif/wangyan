package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.service.SpecGroupService;
import com.mr.service.SpecParamService;
import com.mr.service.pojo.SpecGroup;
import com.mr.service.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecGroupService service;

    @Autowired
    private SpecParamService paramService;

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupList(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(service.querySpecGroupByCid(cid));
    }
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParamsList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic",required = false) Boolean generic
    ){
         List<SpecParam> list = this.paramService.querySpecParamsList(gid,cid,searching,generic);

        if (list == null || list.size() == 0){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        };

        return ResponseEntity.ok(list);
    }

    @PostMapping("group")
    public ResponseEntity<String> add(@RequestBody SpecGroup specGroup){

        if (specGroup==null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        int i = service.add(specGroup);
        return ResponseEntity.ok("新增成功");
    }
    @PostMapping("param")
    public ResponseEntity<String> addParam(@RequestBody SpecParam specParam){


        if (specParam==null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }
        System.out.println(specParam);
        int i = paramService.addParam(specParam);
        System.out.println(i);

        return ResponseEntity.ok("新增成功");
    }


    @PutMapping("group")
    public ResponseEntity<String> update(@RequestBody SpecGroup specGroup){

        if (specGroup==null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        int i = service.update(specGroup);
        System.out.println(i);
        return ResponseEntity.ok("修改成功");
    }
    @PutMapping("param")
    public ResponseEntity<String> updateParam(@RequestBody SpecParam specParam){

        if (specParam==null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        int i = paramService.updateParam(specParam);
        System.out.println(i);
        return ResponseEntity.ok("修改成功");
    }

    @DeleteMapping("group/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){

        if (id==null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        int i = service.delete(id);
        System.out.println(i);
        return ResponseEntity.ok("新增成功");
    }
    @DeleteMapping("param/{id}")
    public ResponseEntity<String> deleteParam(@PathVariable("id") Long id){

        if (id==null){
            throw new MrException(ExceptionEnums.CATEGORY_CANNOT_BE_NULL);
        }

        int i = paramService.deleteParam(id);
        System.out.println(i);
        return ResponseEntity.ok("新增成功");
    }

}
