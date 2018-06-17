package com.youcai.guest.service.impl;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.dataobject.Pricelist;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.dto.excel.pricelist.CategoryExport;
import com.youcai.guest.dto.excel.pricelist.Export;
import com.youcai.guest.dto.excel.pricelist.ProductExport;
import com.youcai.guest.repository.PricelistRepository;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.PricelistService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.vo.pricelist.CategoryVO;
import com.youcai.guest.vo.pricelist.OneVO;
import com.youcai.guest.vo.pricelist.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CategoryService categoryService;
    @Autowired
    private ProductService productService;
    @Override
    public List<Date> findPdates(String guestId) {
        List<Date> dates = pricelistRepository.findDistinctId_PdateById_GuestId(guestId);
        return dates;
    }
    @Override
    public OneVO findLatest(String guestId) {
        List<Date> pdates = this.findPdates(guestId);
        if (pdates!=null && pdates.size()!=0){
            List<Pricelist> pricelists = pricelistRepository.findByIdGuestIdAndIdPdate(guestId, pdates.get(0));
            List<Category> categories = categoryService.findAll();
            Map<String, Product> productMap = productService.findMap();

            OneVO oneVO = new OneVO();
            oneVO.setGuestId(guestId);
            oneVO.setDate(pdates.get(0));
            List<CategoryVO> categoryVOS = new ArrayList<>();
            for (Category category : categories){
                CategoryVO categoryVO = new CategoryVO();
                categoryVO.setCode(category.getCode());
                categoryVO.setName(category.getName());
                List<ProductVO> productVOS = new ArrayList<>();
                for (Pricelist pricelist : pricelists){
                    if (pricelist.getPrice().subtract(BigDecimal.ZERO)
                            .compareTo(new BigDecimal(0.01)) < 0){
                        continue;
                    }
                    Product product = productMap.get(pricelist.getId().getProductId());
                    if (product.getPCode().equals(category.getCode())){
                        ProductVO productVO = new ProductVO();
                        productVO.setId(pricelist.getId().getProductId());
                        productVO.setName(product.getName());
                        productVO.setUnit(product.getUnit());
                        productVO.setPrice(pricelist.getPrice());
                        productVO.setCount(new BigDecimal(1));
                        productVO.setNote(pricelist.getNote());
                        productVOS.add(productVO);
                    }
                }
                categoryVO.setProducts(productVOS);
                categoryVOS.add(categoryVO);
            }
            oneVO.setCategories(categoryVOS);
            return oneVO;
        }else{
            return null;
        }
    }
    @Override
    public Export getExcelExport(String guestId, Date pdate) {
        List<Category> categories = categoryService.findAll();
        List<Pricelist> pricelists = pricelistRepository.findByIdGuestIdAndIdPdate(guestId, pdate);
        Map<String, Product> productMap = productService.findMap();

        Export export = new Export();
        export.setExpire(10);
        export.setPdate(pdate);

        List<CategoryExport> categoryExports = new ArrayList<>();
        for (Category category : categories){
            CategoryExport categoryExport = new CategoryExport();
            categoryExport.setCategoryName(category.getName());
            List<ProductExport> productExports = new ArrayList<>();
            for (Pricelist pricelist : pricelists){
                Product product = productMap.get(pricelist.getId().getProductId());
                if (product.getPCode().equals(category.getCode())){
                    ProductExport productExport = new ProductExport();
                    productExport.setName(product.getName());
                    productExport.setPrice(pricelist.getPrice());
                    productExports.add(productExport);
                }
            }
            categoryExport.setProductExports(productExports);
            categoryExports.add(categoryExport);
        }

        export.setCategoryExports(categoryExports);

        return export;
    }
}
