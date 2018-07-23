package com.youcai.guest.service;

import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dto.excel.order.Export;
import com.youcai.guest.vo.order.OneVO;

import java.util.Date;
import java.util.List;

public interface OrderService {
    void save(String products);
    List<Date> findDates();
    List<String> findStatesByDate(Date date);
    OneVO findOneByDateAndState(Date date, String state);
    void back(Date date);
    boolean isDateRepeat(Date date);
}
