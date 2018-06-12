package com.youcai.guest.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    ;

    private Integer code;

    private String msg;

    ResultEnum() { }

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
