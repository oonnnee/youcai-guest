package com.youcai.guest.repository;

import com.youcai.guest.dataobject.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {

}
