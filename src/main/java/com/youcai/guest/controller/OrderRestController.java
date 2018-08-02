package com.youcai.guest.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dataobject.OrderKey;
import com.youcai.guest.dto.order.NewDTO;
import com.youcai.guest.enums.OrderEnum;
import com.youcai.guest.enums.ResultEnum;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.service.OrderService;
import com.youcai.guest.utils.GuestUtils;
import com.youcai.guest.utils.OrderUtils;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.order.DateAndStatesVO;
import com.youcai.guest.vo.order.OneVO;
import com.youcai.guest.vo.order.OneWithCategoryVO;
import com.youcai.guest.vo.pricelist.CategoryVO;
import com.youcai.guest.vo.pricelist.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/findLatestPricelistWithNum")
    public ResultVO findLatestPricelistWithNum(){
        com.youcai.guest.vo.pricelist.OneVO oneVO = orderService.findLatestPricelistWithNum();

        return ResultVOUtils.success(oneVO, "暂无报价单");
    }

    @GetMapping("/findLatestPricelistWithNumAndCategory")
    public ResultVO findLatestPricelistWithNumAndCategory(){
        com.youcai.guest.vo.pricelist.OneWithCategoryVO oneWithCategoryVO = orderService.findLatestPricelistWithNumAndCategory();

        return ResultVOUtils.success(oneWithCategoryVO, "暂无报价单");
    }

    @PostMapping("/new")
    public ResultVO save(
            @RequestParam String products
    ){
        orderService.save(products);
        return ResultVOUtils.success("创建采购单成功");
    }

    @GetMapping("/findDates")
    public ResultVO<List<Date>> findDates(){
        List<Date> dates = orderService.findDates();
        return ResultVOUtils.success(dates, "未查询到任何日期，暂无采购单");
    }

    @GetMapping("/findStatesByDate")
    public ResultVO<List<String>> findStatesByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        List<String> states = orderService.findStatesByDate(date);
        return ResultVOUtils.success(states, "此日期暂无采购单");
    }

    @GetMapping("/findDatesAndStates")
    public ResultVO<List<DateAndStatesVO>> findDatesAndStates(){
        List<Date> dates = orderService.findDates();
        GuestUtils.HintException(dates, "暂无采购单");

        Integer count = 0;

        List<DateAndStatesVO> dateAndStatesVOS = new ArrayList<>();
        for (Date e : dates)
        {
            DateAndStatesVO dateAndStatesVO = new DateAndStatesVO();

            List<String> states = orderService.findStatesByDate(e);

            count += states.size();

            dateAndStatesVO.setDate(e);
            dateAndStatesVO.setStates(states);

            dateAndStatesVOS.add(dateAndStatesVO);
        }

        return new ResultVO(0, count, "成功", dateAndStatesVOS);
    }

    @GetMapping("/findOneByDateAndState")
    public ResultVO<OneVO> findOneByDateAndState(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        OneVO oneVO = orderService.findOneByDateAndState(date, state);
        return ResultVOUtils.success(oneVO, "未查询到采购单");
    }

    @GetMapping("/findOneWithCategoryByDateAndState")
    public ResultVO<OneVO> findOneWithCategoryByDateAndState(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        OneWithCategoryVO oneWithCategoryVO = orderService.findOneWithCategoryByDateAndState(date, state);
        return ResultVOUtils.success(oneWithCategoryVO, "未查询到采购单");
    }

    @PostMapping("/back")
    public ResultVO back(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        orderService.back(date);
        return ResultVOUtils.success("申请退回成功，请等待后台人员处理");
    }
}
