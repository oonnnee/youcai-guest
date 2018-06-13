package com.youcai.guest.vo.pricelist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVO {
    private String id;
    private String name;
    private String unit;
    private BigDecimal price;
    private BigDecimal count;
    private String note;
}
