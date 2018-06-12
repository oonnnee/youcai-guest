package com.youcai.guest.controller;

import com.youcai.guest.dataobject.Product;
import com.youcai.guest.service.CategoryService;
import com.youcai.guest.service.ProductService;
import com.youcai.guest.utils.ResultVOUtils;
import com.youcai.guest.vo.ResultVO;
import com.youcai.guest.vo.product.OneVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/find")
    public ResultVO<OneVO> findOne(
            @RequestParam String id
    ){
        /*------------ 1.查询 -------------*/
        Product product = productService.findOne(id);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        /*------------ 2.数据拼装 -------------*/
        OneVO OneVO = new OneVO();
        BeanUtils.copyProperties(product, OneVO);
        OneVO.setPCodeName(categoryMap.get(OneVO.getPCode()));

        return ResultVOUtils.success(OneVO);
    }

    @GetMapping("/list")
    public ResultVO<Page<OneVO>> list(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ){
        /*------------ 1.准备 -------------*/
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);

        /*------------ 2.查询 -------------*/
        Page<Product> productPage = productService.findAll(pageable);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        /*------------ 3.数据拼装 -------------*/
        List<OneVO> OneVOS = productPage.getContent().stream().map(e -> {
            OneVO OneVO = new OneVO();
            BeanUtils.copyProperties(e, OneVO);
            OneVO.setPCodeName(categoryMap.get(OneVO.getPCode()));
            return OneVO;
        }).collect(Collectors.toList());
        Page<OneVO> OneVOPage = new PageImpl<OneVO>(OneVOS, pageable, productPage.getTotalElements());

        return ResultVOUtils.success(OneVOPage);
    }


    @GetMapping("/findBy")
    public ResultVO<Page<OneVO>> findByPCodeIn(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "PCodes") String codeStr
    ){
        /*------------ 1.准备 -------------*/
        // 分页
        page = page<0 ? 0:page;
        size = size<=0 ? 10:size;
        Pageable pageable = new PageRequest(page, size);
        // 产品大类编码列表
        List<String> codes = null;
        if (StringUtils.isEmpty(codeStr) == false){
            String[] codeArr = codeStr.split(",");
            codes = Arrays.asList(codeArr);
        }

        /*------------ 2.查询 -------------*/
        Page<Product> productPage = productService.findBy(name, codes, pageable);
        Map<String, String> categoryMap = categoryService.findAllInMap();

        /*------------ 3.数据拼装 -------------*/
        List<OneVO> OneVOS = productPage.getContent().stream().map(e -> {
            OneVO OneVO = new OneVO();
            BeanUtils.copyProperties(e, OneVO);
            OneVO.setPCodeName(categoryMap.get(OneVO.getPCode()));
            return OneVO;
        }).collect(Collectors.toList());
        Page<OneVO> OneVOPage = new PageImpl<OneVO>(OneVOS, pageable, productPage.getTotalElements());

        return ResultVOUtils.success(OneVOPage);
    }
}
