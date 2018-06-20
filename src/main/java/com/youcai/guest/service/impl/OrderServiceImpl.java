package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.dataobject.Order;
import com.youcai.guest.dataobject.Pricelist;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.dto.excel.order.Export;
import com.youcai.guest.dto.excel.order.ProductExport;
import com.youcai.guest.repository.OrderRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.GuestService;
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
    @Autowired
    private GuestService guestService;

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

    @Override
    public Export getExcelExport(String guestId, Date date) {
        Export export = new Export();
        /*------------ 客户名 -------------*/
        export.setGuestName(guestService.findOne(guestId).getName());
        /*------------ 日期 -------------*/
        export.setDate(date);
        /*------------ 产品&采购单金额 -------------*/
        List<ProductExport> productExports = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        List<Order> orders = orderRepository.findByIdGuestIdAndIdOdate(guestId, date);
        Map<String, Product> productMap = productService.findMap();
        int index = 1;
        for (Order order : orders){
            /*--- 产品 ---*/
            Product product = productMap.get(order.getId().getProductId());
            ProductExport productExport = new ProductExport();
            productExport.setIndex(index++);
            productExport.setName(product.getName());
            productExport.setNum(order.getNum());
            productExport.setUnit(product.getUnit());
            productExport.setPrice(order.getPrice());
            productExport.setAmount(order.getAmount());
            productExport.setNote(order.getNote());
            productExports.add(productExport);
            /*--- 采购单金额 ---*/
            amount = amount.add(order.getAmount());
        }
        /*--- 产品 ---*/
        export.setProductExports(productExports);
        /*--- 采购单金额 ---*/
        export.setAmount(amount);
        return export;
    }
}
