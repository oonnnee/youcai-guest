package com.youcai.guest.service;

import com.youcai.guest.dto.excel.pricelist.Export;
import com.youcai.guest.vo.pricelist.OneVO;
import com.youcai.guest.vo.pricelist.OneWithCategoryVO;

import java.util.Date;
import java.util.List;

public interface PricelistService {
    List<Date> findPdates(String guestId);

    OneVO findLatest();
    OneWithCategoryVO findLatestWithCategory();
}
