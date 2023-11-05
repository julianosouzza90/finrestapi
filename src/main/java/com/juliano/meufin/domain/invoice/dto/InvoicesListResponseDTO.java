package com.juliano.meufin.domain.invoice.dto;

import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvoicesListResponseDTO(
        UUID id,
        String name,

        InvoiceTypes type,

        InvoiceStatus status,

        BigDecimal value,

        LocalDateTime due_date,

        LocalDateTime created_at

) {
    public InvoicesListResponseDTO(Invoice invoice) {
        this(invoice.getId(), invoice.getName(), invoice.getType(), invoice.getStatus(), invoice.getValue(), invoice.getDueDate(), invoice.getCreatedAt());
    }
}
