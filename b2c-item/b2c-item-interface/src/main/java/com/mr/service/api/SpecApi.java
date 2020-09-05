package com.mr.service.api;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.common.utils.PageResult;
import com.mr.service.pojo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("spec")
public interface SpecApi {

    @GetMapping("groups/{cid}")
    public List<SpecGroup> querySpecGroupList(@PathVariable("cid") Long cid);


    @GetMapping("params")
    public List<SpecParam> querySpecParamsList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic",required = false) Boolean generic
            );

    @PostMapping("group")
    public String add(@RequestBody SpecGroup specGroup);
    @PostMapping("param")
    public String addParam(@RequestBody SpecParam specParam);


    @PutMapping("group")
    public String update(@RequestBody SpecGroup specGroup);
    @PutMapping("param")
    public String updateParam(@RequestBody SpecParam specParam);

    @DeleteMapping("group/{id}")
    public String delete(@PathVariable("id") Long id);
    @DeleteMapping("param/{id}")
    public String deleteParam(@PathVariable("id") Long id);



}
