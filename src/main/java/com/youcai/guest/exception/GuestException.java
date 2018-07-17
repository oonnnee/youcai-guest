package com.youcai.guest.exception;

import com.youcai.guest.enums.ResultEnum;
import lombok.Getter;

@Getter
public class GuestException extends RuntimeException {

    private Integer code;

    public GuestException(Integer code, String msg){
        super(msg);
        this.code = code;
    }

    public GuestException(String msg){
        super(msg);
        this.code = ResultEnum.ERROR.getCode();
    }
}
