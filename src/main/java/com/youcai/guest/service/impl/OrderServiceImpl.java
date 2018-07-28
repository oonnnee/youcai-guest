package com.youcai.guest.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youcai.guest.dataobject.*;
import com.youcai.guest.dto.order.AllDTO;
import com.youcai.guest.dto.order.NewDTO;
import com.youcai.guest.enums.OrderEnum;
import com.youcai.guest.exception.GuestException;
import com.youcai.guest.repository.OrderRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.OrderService;
import com.youcai.guest.service.PricelistService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.transform.OrderTransform;
import com.youcai.guest.utils.OrderUtils;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.order.CategoryVO;
import com.youcai.guest.vo.order.OneVO;
import com.youcai.guest.vo.order.OneWithCategoryVO;
import com.youcai.guest.vo.order.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
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
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PricelistService pricelistService;

    @Override
    public com.youcai.guest.vo.pricelist.OneVO findLatestPricelistWithNum() {
        com.youcai.guest.vo.pricelist.OneVO oldOneVO = pricelistService.findLatest();

        if (oldOneVO == null){
            return null;
        }else {
            List<com.youcai.guest.vo.pricelist.ProductVO> productVOS = oldOneVO.getProducts();

            com.youcai.guest.vo.pricelist.OneVO oneVO = new com.youcai.guest.vo.pricelist.OneVO();

            String currentGuestId = UserUtils.getCurrentUser().getId();
            List<Order> latestOrders = orderRepository.findLatest(currentGuestId, OrderEnum.DELIVERED.getState());

            for (com.youcai.guest.vo.pricelist.ProductVO productVO : productVOS){
                productVO.setNum(new BigDecimal(0));
                productVO.setNote("");
            }

            if (!CollectionUtils.isEmpty(latestOrders)){
                for (com.youcai.guest.vo.pricelist.ProductVO productVO : productVOS){
                    for (Order order : latestOrders){
                        if (order.getId().getProductId().equals(productVO.getId())){
                            productVO.setNum(order.getNum());
                        }
                    }
                }
            }

            oneVO.setGuestId(currentGuestId);
            oneVO.setDate(oldOneVO.getDate());
            oneVO.setProducts(productVOS);
            return oneVO;
        }
    }

    @Override
    public com.youcai.guest.vo.pricelist.OneWithCategoryVO findLatestPricelistWithNumAndCategory() {
        com.youcai.guest.vo.pricelist.OneWithCategoryVO oldOneWithCategoryVO = pricelistService.findLatestWithCategory();

        if (oldOneWithCategoryVO == null){
            return null;
        }else {
            List<com.youcai.guest.vo.pricelist.CategoryVO> categoryVOS = oldOneWithCategoryVO.getCategoryVOS();

            com.youcai.guest.vo.pricelist.OneWithCategoryVO oneWithCategoryVO = new com.youcai.guest.vo.pricelist.OneWithCategoryVO();

            String currentGuestId = UserUtils.getCurrentUser().getId();
            List<Order> latestOrders = orderRepository.findLatest(currentGuestId, OrderEnum.DELIVERED.getState());

            for (com.youcai.guest.vo.pricelist.CategoryVO categoryVO : categoryVOS) {
                for (com.youcai.guest.vo.pricelist.ProductVO productVO : categoryVO.getProductVOS()) {
                    productVO.setNum(new BigDecimal(0));
                    productVO.setNote("");
                }
            }

            for (com.youcai.guest.vo.pricelist.CategoryVO categoryVO : categoryVOS){
                for (com.youcai.guest.vo.pricelist.ProductVO productVO : categoryVO.getProductVOS()){
                    for (Order order : latestOrders){
                        if (order.getId().getProductId().equals(productVO.getId())){
                            productVO.setNum(order.getNum());
                        }
                    }
                }
            }

            oneWithCategoryVO.setGuestId(currentGuestId);
            oneWithCategoryVO.setDate(oldOneWithCategoryVO.getDate());
            oneWithCategoryVO.setCategoryVOS(categoryVOS);
            return oneWithCategoryVO;
        }
    }

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

        Iterator<NewDTO> it = newDTOS.iterator();
        while(it.hasNext()){
            NewDTO newDTO = it.next();
            if (newDTO.getNum().subtract(BigDecimal.ZERO)
                    .compareTo(new BigDecimal(0.01)) < 0) {
                it.remove();
            }
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

        List<Object[]> objectss = orderRepository.findAllWith(guestId, date, state);
        if (CollectionUtils.isEmpty(objectss)){
            return null;
        }
        List<AllDTO> allDTOS = OrderTransform.objectssToAllDTOS(objectss);

        OneVO oneVO = new OneVO();

        List<ProductVO> products = allDTOS.stream().map( e ->
                new ProductVO(
                        e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                        e.getProductPrice(), e.getProductNum(), e.getProductAmount(), e.getProductImgfile(),
                        e.getNote(), null
                )
        ).collect(Collectors.toList());

        oneVO.setGuestId(guestId);
        oneVO.setDate(date);
        oneVO.setState(state);
        oneVO.setProducts(products);

        return oneVO;
    }
    @Override
    public OneWithCategoryVO findOneWithCategoryByDateAndState(Date date, String state) {
        String guestId = UserUtils.getCurrentUser().getId();

        List<Order> orders = orderRepository.findByIdGuestIdAndIdOdateAndIdState(guestId, date, state);
        if (CollectionUtils.isEmpty(orders)){
            return null;
        }

        Map<String, Product> productMap = productService.findMap();
        List<Category> categories = categoryService.findAll();

        OneWithCategoryVO oneWithCategoryVO = new OneWithCategoryVO();
        
        List categoryVOS = new ArrayList<CategoryVO>();
        for (Category category : categories){
            CategoryVO categoryVO = new CategoryVO();

            List productVOS = new ArrayList<ProductVO>();
            for (Order order : orders){

                Product product = productMap.get(order.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductVO productVO = new ProductVO();

                    productVO.setId(product.getId());
                    productVO.setName(product.getName());
                    productVO.setUnit(product.getUnit());
                    productVO.setPcode(product.getPCode());
                    productVO.setImgfile(product.getImgfile());
                    productVO.setNote(order.getNote());
                    productVO.setPrice(order.getPrice());
                    productVO.setNum(order.getNum());
                    productVO.setAmount(order.getAmount());

                    productVOS.add(productVO);
                }
            }

            categoryVO.setCode(category.getCode());
            categoryVO.setName(category.getName());
            categoryVO.setProductVOS(productVOS);

            categoryVOS.add(categoryVO);
        }

        oneWithCategoryVO.setGuestId(guestId);
        oneWithCategoryVO.setDate(date);
        oneWithCategoryVO.setState(state);
        oneWithCategoryVO.setCategoryVOS(categoryVOS);

        return oneWithCategoryVO;
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

//    @Override
//    public OneVO findOneByDateAndState(Date date, String state) {
//        String guestId = UserUtils.getCurrentUser().getId();
//
//        List<Order> orders = orderRepository.findByIdGuestIdAndIdOdateAndIdState(guestId, date, state);
//        if (CollectionUtils.isEmpty(orders)){
//            return null;
//        }
//
//        Map<String, Product> productMap = productService.findMap();
//
//        OneVO oneVO = new OneVO();
//
//        List<ProductVO> products = new ArrayList<>();
//        for (Order order : orders){
//            Product p = productMap.get(order.getId().getProductId());
//            ProductVO product = new ProductVO();
//            product.setId(order.getId().getProductId());
//            product.setName(p.getName());
//            product.setUnit(p.getUnit());
//            product.setPcode(p.getPCode());
//            product.setPrice(order.getPrice());
//            product.setNum(order.getNum());
//            product.setAmount(order.getAmount());
//            product.setNote(order.getNote());
//            products.add(product);
//        }
//
//        oneVO.setGuestId(guestId);
//        oneVO.setDate(date);
//        oneVO.setState(state);
//        oneVO.setProducts(products);
//
//        return oneVO;
//    }