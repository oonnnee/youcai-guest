package com.youcai.guest.controller;

import com.youcai.guest.service.PricelistService;
import com.youcai.guest.utils.GuestUtils;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.pricelist.CategoryVO;
import com.youcai.guest.vo.pricelist.OneVO;
import com.youcai.guest.vo.pricelist.OneWithCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pricelist")
public class PricelistRestController {

    @Autowired
    private PricelistService pricelistService;

    @GetMapping("/findLatest")
    public ResultVO<OneVO> findLatest(){
        OneVO oneVO = pricelistService.findLatest();
        GuestUtils.HintException(oneVO, "暂无报价单");

        return ResultVOUtils.success(oneVO);
    }

    @GetMapping("/findLatestWithCategory")
    public ResultVO<OneWithCategoryVO> findLatestWithCategory(){
        OneWithCategoryVO oneWithCategoryVO = pricelistService.findLatestWithCategory();
        GuestUtils.HintException(oneWithCategoryVO, "暂无报价单");

        return ResultVOUtils.success(oneWithCategoryVO);
    }
}
