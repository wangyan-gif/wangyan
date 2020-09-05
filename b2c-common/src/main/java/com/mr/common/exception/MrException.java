package com.mr.common.exception;

import com.mr.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MrException extends RuntimeException{

    //继承runtime异常

    private ExceptionEnums exceptionEnums;





}
