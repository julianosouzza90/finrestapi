package com.juliano.meufin.repository;

import com.juliano.meufin.domain.invoice.Balance;
import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Page<Invoice> findAllByUserIdAndCreatedAtBetween(Pageable pageable, UUID userID, LocalDateTime startDate, LocalDateTime endDate);

    Page<Invoice> findByUserIdAndStatusAndTypeAndCreatedAtBetween(Pageable pagination, UUID id, InvoiceStatus status, InvoiceTypes type, LocalDateTime initialDate, LocalDateTime finalDate);

    Page<Invoice> findByUserIdAndTypeAndCreatedAtBetween(Pageable pagination, UUID id, InvoiceTypes type, LocalDateTime initialDate, LocalDateTime finalDate);

    Page<Invoice> findByUserIdAndStatusAndCreatedAtBetween(Pageable pagination, UUID id, InvoiceStatus status, LocalDateTime initialDate, LocalDateTime finalDate);

    Page<Invoice> findByUserIdAndType(Pageable pagination, UUID id, InvoiceTypes type);

    Page<Invoice> findByUserIdAndStatus(Pageable pagination, UUID id, InvoiceStatus status);

    Page<Invoice> findByUserIdAndTypeAndStatus(Pageable pagination, UUID id, InvoiceTypes type, InvoiceStatus status);

    Page<Invoice> findByUserId(Pageable pagination, UUID id);


    List<Invoice> getBalanceByUserId(UUID userId);

    List<Invoice> getBalanceByUserIdAndWalletId(UUID id, UUID walletId);
}
