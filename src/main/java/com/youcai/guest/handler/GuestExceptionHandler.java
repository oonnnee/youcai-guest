package com.youcai.guest.handler;

import com.youcai.guest.exception.GuestException;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GuestExceptionHandler {

    @ExceptionHandler(GuestException.class)
    @ResponseBody
    public ResultVO handleGuestException(GuestException e){
        return ResultVOUtils.error(e.getCode(), e.getMessage());
    }
}
