package com.youcai.guest.vo.deliver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.youcai.guest.dataobject.Driver;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OneWithCategoryVO {

    private String guestId;
    private Date deliverDate;
    private Date orderDate;
    private Driver driver;
    private String state;

    @JsonProperty("categories")
    private List<CategoryVO> categoryVOS;

}
