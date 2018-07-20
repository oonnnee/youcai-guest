package com.youcai.guest.test;

import com.youcai.guest.exception.GuestException;
import org.junit.Test;


public class JTest {

    @Test
    public void test(){
        try {
            exception();
        }catch (GuestException exception){
            System.out.println("--------");
        }

    }

    private void exception(){
        throw new GuestException("123");
    }

}
