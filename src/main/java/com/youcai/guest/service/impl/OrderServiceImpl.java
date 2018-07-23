package com.youcai.guest.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dataobject.OrderKey;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.dto.order.NewDTO;
import com.youcai.guest.enums.OrderEnum;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.repository.OrderRepository;
import com.youcai.guest.service.OrderService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.utils.OrderUtils;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.order.OneVO;
import com.youcai.guest.vo.order.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

    @Override
    public void save(String products) {
        List<NewDTO> newDTOS;
        try {
            newDTOS = new Gson().fromJson(products,
                    new TypeToken<List<NewDTO>>() {
                    }.getType());
        } catch (Exception e) {
            throw new GuestException("创建采购单失败，产品参数错误");
        }

        Date now = new Date();
        if (this.isDateRepeat(now)){
            throw new GuestException("创建采购单失败，今天您已经进行采购了哦");
        }

        Guest guest = UserUtils.getCurrentUser();
        List<Order> orders = newDTOS.stream().map(e ->
                new Order(new OrderKey(now, guest.getId(), e.getProductId(), OrderUtils.getStateNew()), e.getPrice(), e.getNum(),
                        e.getPrice().multiply(e.getNum()), e.getNote())
        ).collect(Collectors.toList());

        orderRepository.save(orders);
    }
    @Override
    public List<Date> findDates() {
        Guest currentUser = UserUtils.getCurrentUser();
        List<Date> dates = orderRepository.findDistinctIdOdateByIdGuestId(currentUser.getId());
        return dates;
    }

    @Override
    public OneVO findOneByDateAndState(Date date, String state) {
        String guestId = UserUtils.getCurrentUser().getId();
        List<Order> orders = orderRepository.findByIdGuestIdAndIdOdateAndIdState(guestId, date, state);
        if (CollectionUtils.isEmpty(orders)){
            return null;
        }
        Map<String, Product> productMap = productService.findMap();

        OneVO oneVO = new OneVO();

        List<ProductVO> products = new ArrayList<>();
        for (Order order : orders){
            Product p = productMap.get(order.getId().getProductId());
            ProductVO product = new ProductVO();
            product.setId(order.getId().getProductId());
            product.setName(p.getName());
            product.setUnit(p.getUnit());
            product.setPrice(order.getPrice());
            product.setNum(order.getNum());
            product.setAmount(order.getPrice().multiply(order.getNum()));
            product.setNote(order.getNote());
            products.add(product);
        }

        oneVO.setGuestId(guestId);
        oneVO.setDate(date);
        oneVO.setState(state);
        oneVO.setProducts(products);

        return oneVO;
    }

    @Override
    @Transactional
    public void back(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();
        String state = OrderUtils.getStateBacking();
        orderRepository.back(guestId, date, state, OrderEnum.NEW.getState());
    }

    @Override
    public List<String> findStatesByDate(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();
        return orderRepository.findDistinctIdStateByIdGuestIdAndIdOdate(guestId, date);
    }

    @Override
    public boolean isDateRepeat(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();
        List states = Arrays.asList(OrderEnum.NEW.getState(), OrderEnum.DELIVERED.getState());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return orderRepository.find(guestId, date, states)!=null;
    }
}
