package com.youcai.guest.service;

import com.youcai.guest.vo.deliver.OneVO;

import java.util.Date;
import java.util.List;

public interface DeliverService {
    List<Date> findDates();
    OneVO findOneByDate(Date date);
}
