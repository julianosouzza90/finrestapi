package com.juliano.meufin.domain.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryDTO(@NotBlank  String name) {
}
