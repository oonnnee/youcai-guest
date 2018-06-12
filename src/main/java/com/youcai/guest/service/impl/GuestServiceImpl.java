package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.repository.GuestRepository;
import com.youcai.guest.service.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
