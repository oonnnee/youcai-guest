package com.youcai.guest.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.guest.dto.order.NewDTO;
import com.youcai.guest.exception.GuestException;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class JTest {

    @Test
    public void test(){
        String text = "{'code':0,'msg':'成功','data':['2018-07-24','2018-07-23','2018-07-22','2018-07-21','2018-03-01','2018-01-01']}";
        Obj obj = null;
        try {
            obj = new Gson().fromJson(text,
                    new TypeToken<Obj>() {
                    }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(obj);
    }

    private void exception(){
        throw new GuestException("123");
    }

}
