package com.juliano.meufin.domain.category.dto;

import com.juliano.meufin.domain.category.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryDetailsDTO(UUID id, String name, LocalDateTime created_at, UUID user_id) {
    public CategoryDetailsDTO(Category category) {
        this(category.getId(), category.getName(), category.getCreatedAt(), category.getUser().getId());
    }
}
