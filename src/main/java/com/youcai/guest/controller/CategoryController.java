package com.youcai.guest.controller;

import com.youcai.guest.dataobject.Category;
import com.youcai.guest.dataobject.Product;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.category.ListWithProductsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    @GetMapping("/map")
    public ResultVO<Map<String, String>> map(){
        Map<String, String> categoryMap = categoryService.findAllInMap();
        return ResultVOUtils.success(categoryMap);
    }

    @GetMapping("/list")
    public ResultVO<List<Category>> list(){
        List<Category> categories = categoryService.findAll();
        return ResultVOUtils.success(categories);
    }

    @GetMapping("/listWithProducts")
    public ResultVO<List<ListWithProductsVO>> listWithProducts(){
        /*------------ 1.查询数据 -------------*/
        // 产品大类数据
        List<Category> categories = categoryService.findAll();
        // 产品列表数据
        List<Product> products = productService.findAll();
        /*------------ 2.数据拼装 -------------*/
        List<ListWithProductsVO> listWithProductsVOS = new ArrayList<>();
        for (Category category : categories){
            ListWithProductsVO categoryWithProductsVO = new ListWithProductsVO();
            categoryWithProductsVO.setCategoryCode(category.getCode());
            categoryWithProductsVO.setCategoryName(category.getName());
            categoryWithProductsVO.setNote(category.getNote());
            categoryWithProductsVO.setProducts(new ArrayList<>());
            listWithProductsVOS.add(categoryWithProductsVO);
        }
        for (Product product : products){
            for (ListWithProductsVO listWithProductsVO : listWithProductsVOS){
                if (listWithProductsVO.getCategoryCode().equals(product.getPCode())){
                    listWithProductsVO.getProducts().add(product);
                    break;
                }
            }
        }
        return ResultVOUtils.success(listWithProductsVOS);
    }
}
