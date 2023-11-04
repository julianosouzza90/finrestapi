package com.juliano.meufin.domain.user.dto;

import com.juliano.meufin.domain.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record DetailedUserDTO(
        UUID id,
        String name,
        String lastname,
        String login,
        LocalDateTime created_at
) {
    public DetailedUserDTO(User user) {
        this(user.getId(), user.getName(),user.getLastname(), user.getLogin(), user.getCreatedAt());
    }
}
