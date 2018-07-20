package com.youcai.guest.handler;

import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BindExceptionHandler {
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultVO handleGuestException(BindException e){
        return ResultVOUtils.error(e.getAllErrors().get(0).getDefaultMessage());
    }
}