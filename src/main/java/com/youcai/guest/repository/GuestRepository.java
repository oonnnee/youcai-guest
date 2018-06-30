package com.youcai.guest.repository;

import com.youcai.guest.dataobject.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, String> {
    Guest findByPhone(String phone);
}
