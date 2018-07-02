package com.youcai.guest.vo.deliver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryVO {
    private String code;
    private String name;
    private List<ProductVO> products;
}
