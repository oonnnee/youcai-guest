package com.youcai.guest.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dataobject.OrderKey;
import com.youcai.guest.dto.order.NewDTO;
import com.youcai.guest.enums.ResultEnum;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.service.OrderService;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.order.OneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
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
        List<NewDTO> newDTOS = new ArrayList<>();
        try {
            newDTOS = new Gson().fromJson(products,
                    new TypeToken<List<NewDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new GuestException(ResultEnum.ORDER_JSON_PARSE_ERROR);
        }
        Date now = new Date();
        Guest guest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<Order> orders = newDTOS.stream().map(e -> {
            return new Order(new OrderKey(now, guest.getId(), e.getProductId()), e.getPrice(), e.getNum(),
                    e.getPrice().multiply(e.getNum()), e.getNote());
        }).collect(Collectors.toList());
        orderService.save(orders);
        return ResultVOUtils.success();
    }

    @GetMapping("/findDates")
    public ResultVO<List<Date>> findDates(){
        Guest guest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<Date> dates = orderService.findDatesByGuestId(guest.getId());
        return ResultVOUtils.success(dates);
    }

    @GetMapping("/findOneByDate")
    public ResultVO<OneVO> findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        Guest guest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        OneVO oneVO = orderService.findByGuestIdAndDate(guest.getId(), date);
        return ResultVOUtils.success(oneVO);
    }

    @PostMapping("/delete")
    public ResultVO delete(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        Guest guest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        orderService.delete(guest.getId(), date);
        return ResultVOUtils.success();
    }
}
