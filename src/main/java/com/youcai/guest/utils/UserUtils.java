package com.youcai.guest.utils;

import com.youcai.guest.dataobject.Guest;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {
    public static Guest getCurrentUser(){
        return (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
