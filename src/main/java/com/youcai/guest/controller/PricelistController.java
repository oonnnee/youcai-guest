package com.youcai.guest.controller;

import com.youcai.guest.service.PricelistService;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.pricelist.OneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pricelist")
public class PricelistController {
    @Autowired
    private PricelistService pricelistService;
    @GetMapping("/findLatest")
    public ResultVO<OneVO> findLatest(
            @RequestParam String guestId
    ){
        OneVO oneVO = pricelistService.findLatest(guestId);
        return ResultVOUtils.success(oneVO);
    }
}
