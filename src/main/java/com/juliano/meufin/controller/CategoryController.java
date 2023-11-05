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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
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
    @GetMapping
    public ResponseEntity<Page<CategoryDetailsDTO>> list(Pageable pagination) {
        User user = AuthenticatedUser.get();
        Page<Category> categories = this.categoryRepository.findByUserId(pagination, user.getId());

        return ResponseEntity.ok(categories.map(CategoryDetailsDTO::new));
    }
}
