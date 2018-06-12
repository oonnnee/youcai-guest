package com.youcai.guest.vo.pricelist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {
    private String code;
    private String name;
    private List<ProductVO> products;
}
