package com.youcai.guest.utils;

import com.youcai.guest.exception.GuestException;
import com.youcai.guest.exception.HintException;

import java.math.BigDecimal;

public class GuestUtils {
    public static void GuestException(Object object, String msg){
        if (object == null){
            throw new GuestException(msg);
        }
    }
    public static void GuestException(boolean b, String msg){
        if (b){
            throw new GuestException(msg);
        }
    }
    public static void HintException(Object object, String msg){
        if (object == null){
            throw new HintException(msg);
        }
    }

    public static boolean isZero(BigDecimal num){
        return num.subtract(BigDecimal.ZERO).compareTo(new BigDecimal(0.01)) < 0;
    }
    public static boolean isNegative(BigDecimal num){
        return num.compareTo(BigDecimal.ZERO) < 0;
    }
}
