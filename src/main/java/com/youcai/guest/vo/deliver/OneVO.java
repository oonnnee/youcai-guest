package com.youcai.guest.vo.deliver;

import com.youcai.guest.dataobject.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OneVO {
    private String guestId;
    private Date deliverDate;
    private Date orderDate;
    private Driver driver;
    private String state;
    private BigDecimal total;
    private List<ProductVO> products;
}
