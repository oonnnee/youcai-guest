package com.youcai.guest.vo.order;

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
    private String state;
    private List<ProductVO> products;
}
