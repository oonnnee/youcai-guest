package com.youcai.guest.exception;

import com.youcai.guest.enums.ResultEnum;
import lombok.Getter;

@Getter
public class HintException extends RuntimeException {

    public HintException(String msg){
        super(msg);
    }
}
