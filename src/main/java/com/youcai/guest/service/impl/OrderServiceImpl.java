package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dataobject.Pricelist;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.repository.OrderRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.OrderService;
import com.youcai.guest.service.ProductService;
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
    @Override
    public void save(List<Order> orders) {
        orderRepository.save(orders);
    }
    @Override
    public List<Date> findDatesByGuestId(String guestId) {
        List<Date> dates = orderRepository.findDistinctIdOdateByIdGuestId(guestId);
        return dates;
    }

    @Override
    public OneVO findByGuestIdAndDate(String guestId, Date date) {
        List<Order> orders = orderRepository.findByIdGuestIdAndIdOdate(guestId, date);
        List<Category> categories = categoryService.findAll();
        Map<String, Product> productMap = productService.findMap();

        OneVO oneVO = new OneVO();
        oneVO.setGuestId(guestId);
        oneVO.setDate(date);
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
    public void delete(String guestId, Date date) {
        orderRepository.deleteByIdGuestIdAndIdOdate(guestId, date);
    }
}
