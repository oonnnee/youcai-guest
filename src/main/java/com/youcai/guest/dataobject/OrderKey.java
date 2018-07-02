package com.youcai.guest.dataobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * 客户采购单联合主键
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderKey implements Serializable {

    /*--- 采购单日期 ---*/
    private Date odate;

    /*--- 采购单客户id ---*/
    private String guestId;

    /*--- 采购单产品id ---*/
    private String productId;

    /*--- 采购单状态, "0"正常, 其它退回 ---*/
    private String state;
}
