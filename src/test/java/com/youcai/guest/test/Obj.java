package com.youcai.guest.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Obj {
    private Integer code;
    private String msg;
    private List<String> data;
}
