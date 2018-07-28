package com.youcai.guest.vo.order;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Date date;
    private String state;

    @JsonProperty("categories")
    private List<CategoryVO> categoryVOS;

}
