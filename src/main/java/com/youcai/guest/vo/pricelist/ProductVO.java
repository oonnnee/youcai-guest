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
    private String category;
    private String unit;
    private BigDecimal marketPrice;
    private BigDecimal guestPrice;
    private String imgfile;
    private String note;
    private String pcode;
    private BigDecimal num;
}
