package com.youcai.guest.repository;

import com.youcai.guest.enums.OrderEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void back() throws ParseException {
        String guestId = "4075324994";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        List states = Arrays.asList(OrderEnum.DELIVERED.getState(), OrderEnum.NEW.getState());
        String res = orderRepository.find(guestId, date, states);
        System.out.println(res);
    }
}