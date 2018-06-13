package com.youcai.guest.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewDTO {
    @JsonProperty("id")
    private String productId;
    private BigDecimal price;
    private BigDecimal num;
    private String note;
}
