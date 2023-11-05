package com.juliano.meufin.repository;

import com.juliano.meufin.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameAndUserId(String name, UUID userID);

    Page<Category> findByUserId(Pageable pagination , UUID id);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
