package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.dataobject.Pricelist;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.dto.pricelist.AllDTO;
import com.youcai.guest.repository.PricelistRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.PricelistService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.transform.PricelistTransform;
import com.youcai.guest.utils.GuestUtils;
import com.youcai.guest.utils.UserUtils;
import com.youcai.guest.vo.pricelist.CategoryVO;
import com.youcai.guest.vo.pricelist.OneVO;
import com.youcai.guest.vo.pricelist.OneWithCategoryVO;
import com.youcai.guest.vo.pricelist.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PricelistServiceImpl implements PricelistService {
    @Autowired
    private PricelistRepository pricelistRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Override
    public List<Date> findPdates(String guestId) {
        List<Date> dates = pricelistRepository.findDistinctId_PdateById_GuestId(guestId);
        return dates;
    }
    @Override
    public OneVO findLatest() {
        String guestId = UserUtils.getCurrentUser().getId();

        List<Date> pdates = this.findPdates(guestId);
        if (CollectionUtils.isEmpty(pdates) == false){
            List<Object[]> objectss = pricelistRepository.findAllWith(guestId, pdates.get(0));
            List<AllDTO> allDTOS = PricelistTransform.objectssToAllDTOS(objectss);

            OneVO oneVO = new OneVO();

            List<ProductVO> products = new ArrayList<>();
            for (AllDTO e : allDTOS){
                if (!GuestUtils.isZero(e.getProductGuestPrice())) {
                    products.add(new ProductVO(
                            e.getProductId(), e.getProductName(), e.getProductCategory(), e.getProductUnit(),
                            e.getProductMarketPrice(), e.getProductGuestPrice(), e.getProductImgfile(), e.getNote(),
                            null, null
                    ));
                }
            }

            oneVO.setGuestId(guestId);
            oneVO.setDate(pdates.get(0));
            oneVO.setProducts(products);

            return oneVO;
        }else{
            return null;
        }
    }

    @Override
    public OneWithCategoryVO findLatestWithCategory() {
        String guestId = UserUtils.getCurrentUser().getId();

        List<Date> pdates = this.findPdates(guestId);
        if (CollectionUtils.isEmpty(pdates) == false){
            OneWithCategoryVO oneWithCategoryVO = new OneWithCategoryVO();

            List<Pricelist> pricelists = pricelistRepository.findByIdGuestIdAndIdPdate(guestId, pdates.get(0));
            Map<String, Product> productMap = productService.findMap();
            List<Category> categories = categoryService.findAll();

            List categoryVOS = new ArrayList<CategoryVO>();
            for (Category category : categories){
                CategoryVO categoryVO = new CategoryVO();

                List productVOS = new ArrayList<ProductVO>();
                for (Pricelist pricelist : pricelists){
                    if (GuestUtils.isZero(pricelist.getPrice())) {
                        continue;
                    }

                    Product product = productMap.get(pricelist.getId().getProductId());
                    if (product.getPCode().equals(category.getCode())){
                        ProductVO productVO = new ProductVO();

                        productVO.setId(product.getId());
                        productVO.setName(product.getName());
                        productVO.setUnit(product.getUnit());
                        productVO.setPcode(product.getPCode());
                        productVO.setImgfile(product.getImgfile());
                        productVO.setNote(pricelist.getNote());
                        productVO.setGuestPrice(pricelist.getPrice());
                        productVO.setMarketPrice(product.getPrice());

                        productVOS.add(productVO);
                    }
                }

                categoryVO.setCode(category.getCode());
                categoryVO.setName(category.getName());
                categoryVO.setProductVOS(productVOS);

                categoryVOS.add(categoryVO);
            }

            oneWithCategoryVO.setGuestId(guestId);
            oneWithCategoryVO.setDate(pdates.get(0));
            oneWithCategoryVO.setCategoryVOS(categoryVOS);

            return oneWithCategoryVO;
        }else{
            return null;
        }

    }
}
