package com.project01.skillineserver.repository;

import com.project01.skillineserver.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    void deleteByIdIn(List<Long> id);
}
