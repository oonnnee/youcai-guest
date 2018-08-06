package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.*;
import com.youcai.guest.dto.deliver.AllDTO;
import com.youcai.guest.repository.DeliverRepository;
import com.youcai.guest.service.*;
import com.youcai.guest.transform.DeliverTransform;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.deliver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeliverServiceImpl implements DeliverService {
    @Autowired
    private DeliverRepository deliverRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private GuestService guestService;
    @Autowired
    private DriverService driverService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public List<Date> findDates() {
        Guest currentUser = UserUtils.getCurrentUser();
        List<Date> dates = deliverRepository.findDistinctIdDdateByIdGuestId(currentUser.getId());
        return dates;
    }
    @Override
    public List<DateAndStateVO> findDatesAndState() {
        Guest currentUser = UserUtils.getCurrentUser();
        List<Object[]> objectss = deliverRepository.findDistinctDateAndStateByGuestId(currentUser.getId());
        List<DateAndStateVO> dateAndStateVOS = objectss.stream().map(e -> new DateAndStateVO((Date) e[0], (String) e[1])).collect(Collectors.toList());
        return dateAndStateVOS;
    }

    @Override
    public OneVO findOneByDate(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();

        List<Object[]> objectss = deliverRepository.findAllWith(guestId, date);
        if (CollectionUtils.isEmpty(objectss)) {
            return null;
        }
        List<AllDTO> allDTOS = DeliverTransform.objectssToAllDTOS(objectss);

        Driver driver = driverService.findOne(allDTOS.get(0).getDriverId());

        OneVO oneVO = new OneVO();

        BigDecimal total = BigDecimal.ZERO;

        List<ProductVO> products = new ArrayList<>();
        for (AllDTO e : allDTOS){
            total = total.add(e.getProductAmount());
            products.add(new ProductVO(
                    e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                    e.getProductPrice(), e.getProductNum(), e.getProductAmount(), e.getProductImgfile(),
                    e.getNote(), null
            ));
        }

        oneVO.setGuestId(guestId);
        oneVO.setDeliverDate(allDTOS.get(0).getDeliverDate());
        oneVO.setOrderDate(allDTOS.get(0).getOrderDate());
        oneVO.setDriver(driver);
        oneVO.setState(allDTOS.get(0).getState());
        oneVO.setTotal(total);
        oneVO.setProducts(products);

        return oneVO;
    }

    @Override
    public OneWithCategoryVO findOneWithCategoryByDate(Date date) {
        String guestId = UserUtils.getCurrentUser().getId();

        List<DeliverList> delivers = deliverRepository.findByIdGuestIdAndIdOrderDate(guestId, date);
        if (CollectionUtils.isEmpty(delivers)) {
            return null;
        }

        Map<String, Product> productMap = productService.findMap();
        Driver driver = driverService.findOne(delivers.get(0).getId().getDriverId());
        List<Category> categories = categoryService.findAll();

        OneWithCategoryVO oneWithCategoryVO = new OneWithCategoryVO();

        List categoryVOS = new ArrayList<CategoryVO>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();

            List productVOS = new ArrayList<ProductVO>();
            for (DeliverList deliver : delivers){

                Product product = productMap.get(deliver.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();

                    productVO.setId(product.getId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPcode(product.getPCode());
                    productVO.setImgfile(product.getImgfile());
                    productVO.setNote(deliver.getNote());
                    productVO.setPrice(deliver.getPrice());
                    productVO.setNum(deliver.getNum());
                    productVO.setAmount(deliver.getAmount());

                    productVOS.add(productVO);
                }
            }

            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            categoryVO.setProductVOS(productVOS);

            categoryVOS.add(categoryVO);
        }
        
        oneWithCategoryVO.setGuestId(guestId);
        oneWithCategoryVO.setDeliverDate(delivers.get(0).getId().getDdate());
        oneWithCategoryVO.setOrderDate(delivers.get(0).getId().getOrderDate());
        oneWithCategoryVO.setDriver(driver);
        oneWithCategoryVO.setState(delivers.get(0).getId().getState());
        oneWithCategoryVO.setCategoryVOS(categoryVOS);
        
        return oneWithCategoryVO;
    }

    @Override
    @Transactional
    public void updateState(Date date, String oldState, String newState) {
        Guest currentUser = UserUtils.getCurrentUser();
        deliverRepository.updateState(currentUser.getId(), date, oldState, newState);
    }

}

//    @Override
//    public OneVO findOneByDate(Date date) {
//        String guestId = UserUtils.getCurrentUser().getId();
//
//        List<DeliverList> delivers = deliverRepository.findByIdGuestIdAndIdDdate(guestId, date);
//        if (CollectionUtils.isEmpty(delivers)) {
//            return null;
//        }
//
//        Map<String, Product> productMap = productService.findMap();
//        Driver driver = driverService.findOne(delivers.get(0).getId().getDriverId());
//
//        OneVO oneVO = new OneVO();
//
//        List<ProductVO> products = new ArrayList<>();
//        for (DeliverList deliver : delivers) {
//            Product p = productMap.get(deliver.getId().getProductId());
//            ProductVO product = new ProductVO();
//            product.setId(deliver.getId().getProductId());
//            product.setName(p.getName());
//            product.setUnit(p.getUnit());
//            product.setPcode(p.getPCode());
//            product.setPrice(deliver.getPrice());
//            product.setNum(deliver.getNum());
//            product.setAmount(deliver.getAmount());
//            product.setNote(deliver.getNote());
//            products.add(product);
//        }
//
//        oneVO.setGuestId(guestId);
//        oneVO.setDate(date);
//        oneVO.setDriver(driver);
//        oneVO.setState(delivers.get(0).getId().getState());
//        oneVO.setProducts(products);
//
//        return oneVO;
//    }