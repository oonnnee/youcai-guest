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
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.order.OneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/new")
    public ResultVO save(
            @RequestParam String products
    ){
        List<NewDTO> newDTOS;
        try {
            newDTOS = new Gson().fromJson(products,
                    new TypeToken<List<NewDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new GuestException("创建采购单失败，产品json解析错误");
        }
        Date now = new Date();
        Guest guest = UserUtils.getCurrentUser();
        List<Order> orders = newDTOS.stream().map(e ->
             new Order(new OrderKey(now, guest.getId(), e.getProductId(), OrderEnum.OK.getState()), e.getPrice(), e.getNum(),
                    e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());
        orderService.save(orders);
        return ResultVOUtils.success();
    }

    @GetMapping("/findDates")
    public ResultVO<List<Date>> findDates(){
        Guest guest = UserUtils.getCurrentUser();
        List<Date> dates = orderService.findDates();
        return ResultVOUtils.success(dates);
    }

    // TODO 更新api
    @GetMapping("/findStatesByDate")
    public ResultVO<List<String>> findStatesByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        return ResultVOUtils.success(orderService.findStatesByDate(date));
    }

    // TODO 更新api
    @GetMapping("/findOneByDateAndState")
    public ResultVO<OneVO> findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam String state
    ){
        OneVO oneVO = orderService.findOneByDateAndState(date, state);
        return ResultVOUtils.success(oneVO);
    }

    // TODO 更新api
    @PostMapping("/back")
    public ResultVO back(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        orderService.back(date);
        return ResultVOUtils.success();
    }
}
