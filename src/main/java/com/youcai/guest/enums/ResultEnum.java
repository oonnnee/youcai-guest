package com.youcai.guest.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    NO_LOGIN(1, "未登录"),
    LOGIN_ERROR(2, "登录失败，用户名或密码错误"),
    ORDER_JSON_PARSE_ERROR(3, "创建订单时，json解析错误"),
    UPDATE_USER_INFO_ID_ERROR(4, "更新用户信息，id与当前登录用户id不一致"),
    UPDATE_USER_PWD_INPUT_NOT_SAME(5, "两次密码输入不一致"),
    UPDATE_USER_PWD_OLD_PWD_ERROR(5, "原密码错误")
    ;

    private Integer code;

    private String msg;

    ResultEnum() { }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
