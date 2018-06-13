package com.youcai.guest.service;

import com.youcai.guest.dataobject.Order;
import com.youcai.guest.vo.order.OneVO;

import java.util.Date;
import java.util.List;

public interface OrderService {
    void save(List<Order> orders);
    List<Date> findDatesByGuestId(String guestId);
    OneVO findByGuestIdAndDate(String guestId, Date date);
}
