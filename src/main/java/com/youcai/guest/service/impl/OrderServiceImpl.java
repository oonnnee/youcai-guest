package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.*;
import com.youcai.guest.dto.excel.order.Export;
import com.youcai.guest.dto.excel.order.ProductExport;
import com.youcai.guest.enums.OrderEnum;
import com.youcai.guest.repository.OrderRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.GuestService;
import com.youcai.guest.service.OrderService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.utils.UUIDUtils;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.order.CategoryVO;
import com.youcai.guest.vo.order.OneVO;
import com.youcai.guest.vo.order.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GuestService guestService;

    @Override
    public void save(List<Order> orders) {
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
        List<Category> categories = categoryService.findAll();
        Map<String, Product> productMap = productService.findMap();

        OneVO oneVO = new OneVO();
        oneVO.setGuestId(guestId);
        oneVO.setDate(date);
        oneVO.setState(state);
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            List<ProductVO> productVOS = new ArrayList<>();
            for (Order order : orders){
                Product product = productMap.get(order.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();
                    productVO.setId(order.getId().getProductId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPrice(order.getPrice());
                    productVO.setNum(order.getNum());
                    productVO.setAmount(order.getPrice().multiply(order.getNum()));
                    productVO.setNote(order.getNote());
                    productVOS.add(productVO);
                }
            }
            categoryVO.setProducts(productVOS);
            categoryVOS.add(categoryVO);
        }
        oneVO.setCategories(categoryVOS);
        return oneVO;
    }

    @Override
    @Transactional
    public void back(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();
        String state = UUIDUtils.randomUUID();
        orderRepository.back(guestId, date, state, OrderEnum.OK.getState());
    }

    @Override
    public List<String> findStatesByDate(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();
        return orderRepository.findDistinctIdStateByIdGuestIdAndIdOdate(guestId, date);
    }
}
