package com.juliano.meufin.service;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category create(Category category) throws ConflictException {

        if(categoryRepository.existsByNameAndUserId(category.getName(), category.getUser().getId())) {
            throw  new ConflictException("Já existe uma categoria cadastra com o nome " + category.getName());
        }

        return  this.categoryRepository.save(category);

    }
}
