package com.juliano.meufin.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthUserDTO(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
