package com.youcai.guest.service;

import com.youcai.guest.vo.order.OneVO;
import com.youcai.guest.vo.order.OneWithCategoryVO;

import java.util.Date;
import java.util.List;

public interface OrderService {
    void save(String products);
    List<Date> findDates();
    List<String> findStatesByDate(Date date);

    OneVO findOneByDateAndState(Date date, String state);
    OneWithCategoryVO findOneWithCategoryByDateAndState(Date date, String state);

    void back(Date date);
    boolean isDateRepeat(Date date);

    com.youcai.guest.vo.pricelist.OneVO findLatestPricelistWithNum();
    com.youcai.guest.vo.pricelist.OneWithCategoryVO findLatestPricelistWithNumAndCategory();
}
