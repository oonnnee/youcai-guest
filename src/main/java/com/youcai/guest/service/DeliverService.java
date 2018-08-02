package com.youcai.guest.service;

import com.youcai.guest.vo.deliver.DateAndStateVO;
import com.youcai.guest.vo.deliver.OneVO;
import com.youcai.guest.vo.deliver.OneWithCategoryVO;

import java.util.Date;
import java.util.List;

public interface DeliverService {
    List<Date> findDates();
    List<DateAndStateVO> findDatesAndState();

    OneVO findOneByDate(Date date);
    OneWithCategoryVO findOneWithCategoryByDate(Date date);

    void updateState(Date date, String oldState, String newState);
}
