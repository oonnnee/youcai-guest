package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.repository.GuestRepository;
import com.youcai.guest.service.GuestService;
import com.youcai.guest.utils.EDSUtils;
import com.youcai.guest.utils.GuestUtils;
import com.youcai.guest.utils.KeyUtils;
import com.youcai.guest.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.applet.Main;
import sun.security.util.KeyUtil;

import javax.transaction.Transactional;

@Service
public class GuestServiceImpl implements GuestService, UserDetailsService {

    private void checkPhone(String phone){
        GuestUtils.GuestException(this.isPhoneRepeat(phone), "该手机号已被注册");
    }
    private void checkPhone(String phone, String id){
        GuestUtils.GuestException(this.isPhoneRepeat(phone, id), "该手机号已被注册");
    }

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Guest guest = null;
        if (!username.equals("admin")){
            guest = guestRepository.findByPhone(username);
        }
        return guest;
    }

    @Override
    @Transactional
    public Guest update(Guest guest) {
        Guest curGuest = UserUtils.getCurrentUser();
        guest.setId(curGuest.getId());

        this.checkPhone(guest.getPhone(), guest.getId());

        Guest oldGuest = guestRepository.findOne(guest.getId());
        guest.setPwd(oldGuest.getPwd());
        if (StringUtils.isEmpty(guest.getPhone())){
            guest.setPhone(oldGuest.getPhone());
        }

        Guest result = guestRepository.save(guest);

        return result;
    }

    @Override
    @Transactional
    public void updatePwd(String pwd) {
        Guest guest = this.findCurrent();
        guest.setPwd(EDSUtils.encryptBasedDes(pwd));

        guestRepository.save(guest);
    }

    @Override
    public Guest findOne(String id) {
        return guestRepository.findOne(id);
    }

    @Override
    @Transactional
    public Guest save(Guest guest) {
        this.checkPhone(guest.getPhone());

        guest.setId(KeyUtils.generate());
        guest.setPwd(EDSUtils.encryptBasedDes(guest.getPwd()));

        Guest result = guestRepository.save(guest);

        return result;
    }

    @Override
    public Guest findCurrent() {
        Guest guest = UserUtils.getCurrentUser();
        return this.findOne(guest.getId());
    }

    @Override
    public boolean isPhoneRepeat(String phone) {
        return guestRepository.findByPhone(phone)!=null;
    }
    @Override
    public boolean isPhoneRepeat(String phone, String id) {
        Guest guest = guestRepository.findByPhone(phone);
        return guest == null ?
                false : !guest.getId().equals(id);
    }
}
