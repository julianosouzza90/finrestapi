package com.juliano.meufin.domain.invoice.dto;

import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateInvoiceResponseDTO(
        UUID id,
        String name,
        InvoiceTypes type,
        InvoiceStatus status,
        BigDecimal value,
        LocalDateTime due_date,
        UUID category_id,
        UUID wallet_id,
        LocalDateTime created_at
) {
    public  CreateInvoiceResponseDTO(Invoice invoice) {
        this( invoice.getId(),
                invoice.getName(),
                invoice.getType(),
                invoice.getStatus(),
                invoice.getValue(),
                invoice.getDueDate(),
                invoice.getCategory().getId(),
                invoice.getWallet().getId(),
                invoice.getCreatedAt());
    }
}
