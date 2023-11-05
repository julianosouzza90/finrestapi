package com.juliano.meufin.domain.invoice.dto;

import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;
import com.juliano.meufin.domain.user.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public record ListInvoicesDTO(Pageable pagination, String startDate, String end_date, InvoiceTypes type, InvoiceStatus status) {
}
