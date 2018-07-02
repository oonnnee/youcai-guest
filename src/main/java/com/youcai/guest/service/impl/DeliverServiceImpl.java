package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.*;
import com.youcai.guest.repository.DeliverRepository;
import com.youcai.guest.service.*;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.deliver.CategoryVO;
import com.youcai.guest.vo.deliver.OneVO;
import com.youcai.guest.vo.deliver.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DeliverServiceImpl implements DeliverService {
    @Autowired
    private DeliverRepository deliverRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private DriverService driverService;

    @Override
    public List<Date> findDates() {
        Guest currentUser = UserUtils.getCurrentUser();
        List<Date> dates = deliverRepository.findDistinctIdDdateByIdGuestId(currentUser.getId());
        return dates;
    }

    @Override
    public OneVO findOneByDate(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();
        List<DeliverList> delivers = deliverRepository.findByIdGuestIdAndIdDdate(guestId, date);
        List<Category> categories = categoryService.findAll();
        Map<String, Product> productMap = productService.findMap();
        Driver driver = driverService.findOne(delivers.get(0).getId().getDriverId());
        Guest guest = guestService.findOne(guestId);

        OneVO oneVO = new OneVO();
        oneVO.setGuestId(guestId);
        oneVO.setGuestName(guest.getName());
        oneVO.setDate(date);
        oneVO.setDriver(driver);
        List<CategoryVO> categoryVOS = new ArrayList<>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            List<ProductVO> productVOS = new ArrayList<>();
            for (DeliverList deliver : delivers){
                Product product = productMap.get(deliver.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();
                    productVO.setId(deliver.getId().getProductId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPrice(deliver.getPrice());
                    productVO.setNum(deliver.getNum());
                    productVO.setAmount(deliver.getPrice().multiply(deliver.getNum()));
                    productVO.setNote(deliver.getNote());
                    productVOS.add(productVO);
                }
            }
            categoryVO.setProducts(productVOS);
            categoryVOS.add(categoryVO);
        }
        oneVO.setCategories(categoryVOS);
        return oneVO;
    }
}
