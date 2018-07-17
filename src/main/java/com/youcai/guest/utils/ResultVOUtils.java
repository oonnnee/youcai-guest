package com.youcai.guest.utils;

import com.youcai.guest.enums.ResultEnum;
import com.youcai.guest.vo.ResultVO;

public class ResultVOUtils {

    public static ResultVO success(Object data){
        return new ResultVO(0,"成功", data);
    }

    public static ResultVO success(){
        return new ResultVO(0, "成功", null);
    }

    public static ResultVO error(String msg){
        return new ResultVO(ResultEnum.ERROR.getCode(), msg, null);
    }

    public static ResultVO error(Integer code, String msg){
        return new ResultVO(code, msg, null);
    }
}
