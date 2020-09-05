package com.mr.web;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.common.utils.PageResult;
import com.mr.service.MemberService;
import com.mr.service.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    private MemberService service;

    @GetMapping("page")
    public ResponseEntity<PageResult> MemberList(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "key",required = false) String key,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",required = false) Boolean desc
    ){
        System.out.println(1111);

        PageResult<User> pageResult = service.MemberList(page,rows,key,sortBy,desc);

        if (pageResult == null && pageResult.getItems().size() == 0){
            throw new MrException(ExceptionEnums.MEMBER_CANNOT_BE_NULL);
        }

        System.out.println(2222);

        return ResponseEntity.ok(pageResult);
    }


}
