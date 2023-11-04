package com.juliano.meufin.controller;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.category.dto.CategoryDetailsDTO;
import com.juliano.meufin.domain.category.dto.CreateCategoryDTO;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.service.CategoryService;
import com.juliano.meufin.util.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDetailsDTO> create(@RequestBody @Valid CreateCategoryDTO data, UriComponentsBuilder uriBuilder) throws ConflictException {
        User user = AuthenticatedUser.get();


        Category createdCategory = this.categoryService.create(new Category(data.name(), user));

        var uri = uriBuilder.cloneBuilder()
                .path("categories/{id}")
                .buildAndExpand(createdCategory.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new CategoryDetailsDTO(createdCategory));
    }
}
