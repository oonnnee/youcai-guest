package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Driver;
import com.youcai.guest.repository.DriverRepository;
import com.youcai.guest.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public Driver findOne(Integer driverId) {
        return driverRepository.findOne(driverId);
    }
}
