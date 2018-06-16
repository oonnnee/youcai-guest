package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.repository.GuestRepository;
import com.youcai.guest.service.GuestService;
import com.youcai.guest.utils.EDSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class GuestServiceImpl implements GuestService, UserDetailsService {

    @Autowired
    private GuestRepository guestRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Guest guest = null;
        if (!username.equals("admin")){
            guest = guestRepository.findOne(username);
        }
        return guest;
    }

    @Override
    @Transactional
    public Guest update(Guest guest) {
        guest.setPwd(guestRepository.findOne(guest.getId()).getPwd());
        Guest result = guestRepository.save(guest);
        result.setPwd(null);
        return result;
    }

    @Override
    @Transactional
    public void updatePwd(String id, String pwd) {
        Guest guest = guestRepository.findOne(id);
        guest.setPwd(EDSUtils.encryptBasedDes(pwd));
        guestRepository.save(guest);
    }

    @Override
    public Guest findOne(String id) {
        return guestRepository.findOne(id);
    }
}
