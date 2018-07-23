package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.dataobject.Guest;
import com.youcai.guest.dataobject.Pricelist;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.repository.PricelistRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.PricelistService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.vo.pricelist.OneVO;
import com.youcai.guest.vo.pricelist.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PricelistServiceImpl implements PricelistService {
    @Autowired
    private PricelistRepository pricelistRepository;
    @Autowired
    private ProductService productService;
    @Override
    public List<Date> findPdates(String guestId) {
        List<Date> dates = pricelistRepository.findDistinctId_PdateById_GuestId(guestId);
        return dates;
    }
    @Override
    public OneVO findLatest() {
        String guestId = ((Guest) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getId();
        List<Date> pdates = this.findPdates(guestId);
        if (CollectionUtils.isEmpty(pdates) == false){
            List<Pricelist> pricelists = pricelistRepository.findByIdGuestIdAndIdPdate(guestId, pdates.get(0));
            Map<String, Product> productMap = productService.findMap();

            OneVO oneVO = new OneVO();

            List<ProductVO> products = new ArrayList<>();
            for (Pricelist pricelist : pricelists) {
                if (pricelist.getPrice().subtract(BigDecimal.ZERO)
                        .compareTo(new BigDecimal(0.01)) < 0) {
                    continue;
                }
                Product p = productMap.get(pricelist.getId().getProductId());
                ProductVO product = new ProductVO();
                product.setId(pricelist.getId().getProductId());
                product.setName(p.getName());
                product.setUnit(p.getUnit());
                product.setPrice(pricelist.getPrice());
                product.setNote(pricelist.getNote());
                products.add(product);
            }

            oneVO.setGuestId(guestId);
            oneVO.setDate(pdates.get(0));
            oneVO.setProducts(products);

            return oneVO;
        }else{
            return null;
        }
    }

}
