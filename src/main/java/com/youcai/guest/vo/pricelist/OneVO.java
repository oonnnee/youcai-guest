package com.youcai.guest.vo.pricelist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneVO {
    private String guestId;
    private Date date;
    private List<ProductVO> products;
}
