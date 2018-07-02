package com.youcai.guest.service;

import com.youcai.guest.dataobject.Guest;

public interface GuestService {
    Guest update(Guest guest);
    void updatePwd(String id, String pwd);
    Guest findOne(String id);
    Guest save(Guest guest);
}
