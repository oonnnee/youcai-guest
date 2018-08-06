package com.youcai.guest.vo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneVO {
    private String guestId;
    private Date date;
    private String state;
    private BigDecimal total;
    private List<ProductVO> products;
}
