package com.juliano.meufin.domain.wallet.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateWalletDTO(
        @NotBlank
        @Length(min = 10, max = 200)
        String name
) {
}
