package com.mr.common.advice;

import com.mr.common.enums.ExceptionEnums;
import com.mr.common.exception.MrException;
import com.mr.common.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {


    @ExceptionHandler(MrException.class)
    public ResponseEntity<ExceptionResult> handException(MrException ex){
        ExceptionEnums exc = ex.getExceptionEnums();

        System.out.println("捕捉到异常:"+ex.getExceptionEnums().getMsg());

//        封装返回结果

        return ResponseEntity.status(exc.getCode()).body(new ExceptionResult(exc));
    }

}
