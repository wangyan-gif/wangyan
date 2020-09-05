package com.mr.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//get方法 有参无参构造
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnums {

    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    NANME_TOO_LONG(500,"名称太长,数据库长度不够"),
    CATEGORY_CANNOT_BE_NULL(404,"没有分类数据"),
    MEMBER_CANNOT_BE_NULL(400,"员工信息不能为空"),
    ADD_GOODS_NULL(500,"新增不成功,出现异常"),
    UPDATE_GOODS_NULL(500,"修改不成功,出现异常"),
    DEL_GOODS_NULL(500,"删除不成功,出现异常"),

    ;

    private int code;//状态码
    private String msg;//提示信息


}
