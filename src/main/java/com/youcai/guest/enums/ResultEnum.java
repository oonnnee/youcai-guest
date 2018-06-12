package com.youcai.guest.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    NO_LOGIN(1, "未登录"),
    LOGIN_ERROR(2, "登录失败，用户名或密码错误")
    ;

    private Integer code;

    private String msg;

    ResultEnum() { }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
