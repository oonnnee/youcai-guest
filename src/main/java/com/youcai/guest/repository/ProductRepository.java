package com.youcai.guest.repository;

import com.youcai.guest.dataobject.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByNameLike(String name, Pageable pageable);

    Page<Product> findByPCodeIn(List<String> pCodes, Pageable pageable);

    Page<Product> findByNameLikeAndPCodeIn(String name, List<String> pCodes, Pageable pageable);

}
