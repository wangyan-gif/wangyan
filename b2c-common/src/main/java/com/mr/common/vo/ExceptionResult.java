package com.mr.common.vo;

import com.mr.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {

    private int code;
    private String msg;
    private Long timestamp;
    private String path;
    private String error;

    public ExceptionResult(ExceptionEnums enums){
        this.code = enums.getCode();
        this.msg = enums.getMsg();
        this.timestamp = System.currentTimeMillis();
    }

}
