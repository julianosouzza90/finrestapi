package com.juliano.meufin.domain.invoice.dto;

import com.juliano.meufin.domain.invoice.InvoiceTypes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateInvoiceDTO(
        @NotBlank
        String  name,
        @NotNull
        UUID category_id,
        @NotNull
        UUID wallet_id,
        @NotNull
        InvoiceTypes type,
        @NotNull
        BigDecimal value,

        @NotNull
        LocalDateTime due_date
) {
}
