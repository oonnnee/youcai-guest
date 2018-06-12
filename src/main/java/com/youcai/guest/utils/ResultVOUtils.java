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

    public static ResultVO error(Integer code, String msg){
        return new ResultVO(code, msg, null);
    }

    public static ResultVO error(ResultEnum resultEnum){
        return new ResultVO(resultEnum.getCode(), resultEnum.getMsg(), null);
    }
}
