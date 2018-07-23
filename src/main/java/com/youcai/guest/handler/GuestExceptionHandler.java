package com.youcai.guest.handler;

import com.youcai.guest.exception.GuestException;
import com.youcai.guest.exception.HintException;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import org.springframework.validation.BindException;
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

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResultVO handleGuestException(BindException e){
        return ResultVOUtils.error(e.getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(HintException.class)
    @ResponseBody
    public ResultVO handleGuestException(HintException e){
        return ResultVOUtils.success(e.getMessage());
    }

}
