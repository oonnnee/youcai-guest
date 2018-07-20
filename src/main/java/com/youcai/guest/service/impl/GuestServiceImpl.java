package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.repository.GuestRepository;
import com.youcai.guest.service.GuestService;
import com.youcai.guest.utils.EDSUtils;
import com.youcai.guest.utils.KeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sun.applet.Main;
import sun.security.util.KeyUtil;

import javax.transaction.Transactional;

@Service
public class GuestServiceImpl implements GuestService, UserDetailsService {

    private void checkPhone(String phone){
        if (this.isPhoneRepeat(phone)){
            throw new GuestException("该手机号已被注册");
        }
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
        this.checkPhone(guest.getPhone());

        Guest curGuest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        guest.setId(curGuest.getId());
        guest.setPwd(guestRepository.findOne(guest.getId()).getPwd());

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
        Guest guest = (Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return this.findOne(guest.getId());
    }

    @Override
    public boolean isPhoneRepeat(String phone) {
        return guestRepository.findByPhone(phone)==null ? false:true;
    }

}
