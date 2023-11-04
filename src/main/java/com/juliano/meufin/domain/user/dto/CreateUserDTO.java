package com.juliano.meufin.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserDTO(
        @NotBlank
        String name,
        @NotBlank
        String lastname,
        @NotBlank
        String login,
        @NotBlank
        String password


) {
}
