package com.youcai.guest.vo.deliver;

import com.youcai.guest.dataobject.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OneVO {
    private String guestId;
    private Date date;
    private Driver driver;
    private String state;
    private List<ProductVO> products;
}
