package com.youcai.guest.controller;

import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.enums.ResultEnum;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.service.GuestService;
import com.youcai.guest.utils.EDSUtils;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("update")
    public ResultVO<Guest> update(Guest guest){
        Guest curGuest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (!curGuest.getId().equals(guest.getId())){
            throw new GuestException("更新用户信息，id与当前登录用户id不一致");
        }
        return ResultVOUtils.success(guestService.update(guest));
    }

    @PostMapping("updatePwd")
    public ResultVO<Guest> update(
            @RequestParam String oldPwd,
            @RequestParam String newPwd,
            @RequestParam String reNewPwd
    ){
        if (!newPwd.equals(reNewPwd)){
            return ResultVOUtils.error("两次密码输入不一致");
        }
        Guest curGuest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (!EDSUtils.encryptBasedDes(oldPwd).equals(guestService.findOne(curGuest.getId()).getPwd())){
            return ResultVOUtils.error("原密码错误");
        }
        guestService.updatePwd(curGuest.getId(), newPwd);
        return ResultVOUtils.success();
    }

    @PostMapping("/register")
    public ResultVO<Guest> register(
            Guest guest,
            @RequestParam String repwd
    ){
        if (!guest.getPwd().equals(repwd)){
            throw new GuestException("两次密码输入不一致");
        }
        Guest result = guestService.save(guest);
        result.setPwd(null);
        return ResultVOUtils.success(result);
    }
}
