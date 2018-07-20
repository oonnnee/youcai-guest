package com.youcai.guest.service;

import com.youcai.guest.dataobject.Guest;

public interface GuestService {
    Guest update(Guest guest);
    void updatePwd(String pwd);
    Guest findOne(String id);
    Guest save(Guest guest);
    Guest findCurrent();
    boolean isPhoneRepeat(String phone);
}
