package com.youcai.guest.vo.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryVO {

    private String code;

    private String name;

    @JsonProperty("products")
    private List<ProductVO> productVOS;

}
