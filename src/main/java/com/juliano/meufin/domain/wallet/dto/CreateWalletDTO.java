package com.juliano.meufin.domain.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateWalletDTO(
        @NotBlank
        @Length(min = 5, max = 100)
        String name
) {
}
