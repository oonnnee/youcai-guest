package com.youcai.guest.controller;

import com.youcai.guest.enums.DeliverEnum;
import com.youcai.guest.service.DeliverService;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.deliver.OneVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/deliver")
public class DeliverRestController {
    @Autowired
    private DeliverService deliverService;

    @GetMapping("/findDates")
    public ResultVO<List<Date>> findDates(){
        List<Date> dates = deliverService.findDates();
        return ResultVOUtils.success(dates);
    }


    // TODO 更新api
    @GetMapping("/findOneByDate")
    public ResultVO<OneVO> findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        OneVO oneVO = deliverService.findOneByDate(date);
        return ResultVOUtils.success(oneVO);
    }

    // TODO 更新api
    @PostMapping("/receive")
    public ResultVO receive(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ){
        deliverService.updateState(date, DeliverEnum.DELIVERING.getState(), DeliverEnum.RECEIVE.getState());
        return ResultVOUtils.success("确认收货成功");
    }

}
