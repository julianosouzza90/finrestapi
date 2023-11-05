package com.juliano.meufin.service;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.CategoryRepository;
import static  org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should be throw a ConflictException When the category name already in use for the user ")
    void createCategoryCase1() {

        Category category = new Category("Category Test", new User());

        when(categoryRepository.existsByNameAndUserId(category.getName(), category.getUser().getId())).thenReturn(true);
        var exception = assertThrows(ConflictException.class,  () -> {
           categoryService.create(category);
        });

        assertEquals(exception.getMessage(), "Já existe uma categoria cadastra com o nome "+category.getName());
        verify(categoryRepository, times(1)).existsByNameAndUserId(category.getName(), category.getUser().getId());

    }

    @Test
    @DisplayName("Should  create a new Category")
    void createCategoryCase2() throws ConflictException {
        Category category = new Category("Category Test 2", new User());

        when(categoryRepository.existsByNameAndUserId(category.getName(), category.getUser().getId())).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        Category result = categoryService.create(category);

        assertEquals(result, category);

        verify(categoryRepository, times(1)).existsByNameAndUserId(category.getName(), category.getUser().getId());
    }
}