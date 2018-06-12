package com.youcai.guest.vo.category;

import com.youcai.guest.dataobject.Product;
import lombok.Data;

import java.util.List;

@Data
public class ListWithProductsVO {

    private String categoryCode;
    private String categoryName;
    private String note;

    private List<Product> products;

}
