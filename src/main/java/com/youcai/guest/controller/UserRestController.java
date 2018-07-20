package com.youcai.guest.controller;

import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.enums.ResultEnum;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.form.guest.RegisterForm;
import com.youcai.guest.form.guest.UpdateForm;
import com.youcai.guest.form.guest.UpdatePwdForm;
import com.youcai.guest.service.GuestService;
import com.youcai.guest.utils.EDSUtils;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("update")
    public ResultVO<Guest> update(
            @Valid UpdateForm form
    ){
        Guest guest = new Guest();
        BeanUtils.copyProperties(form, guest);

        return ResultVOUtils.success(guestService.update(guest));
    }

    @PostMapping("updatePwd")
    public ResultVO<Guest> updatePwd(
            @RequestParam UpdatePwdForm updatePwdForm
    ){
        if (!updatePwdForm.getNewPwd().equals(updatePwdForm.getReNewPwd())){
            return ResultVOUtils.error("两次密码输入不一致");
        }
        if (!EDSUtils.encryptBasedDes(updatePwdForm.getOldPwd()).equals(guestService.findCurrent().getPwd())){
            return ResultVOUtils.error("原密码错误");
        }

        guestService.updatePwd(updatePwdForm.getNewPwd());

        return ResultVOUtils.success("更新密码成功");
    }

    @PostMapping("/register")
    public ResultVO<Guest> register(
            @Valid RegisterForm form,
            @RequestParam String repwd
    ){
        Guest guest = new Guest();
        BeanUtils.copyProperties(form, guest);

        if (!guest.getPwd().equals(repwd)){
            throw new GuestException("两次密码输入不一致");
        }

        Guest result = guestService.save(guest);

        return ResultVOUtils.success(result);
    }
}
