package com.youcai.guest.dto.excel.pricelist;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductExport {
    private String name;
    private BigDecimal price;
}
