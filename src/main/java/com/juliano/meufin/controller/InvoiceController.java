package com.juliano.meufin.controller;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.invoice.Balance;
import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;
import com.juliano.meufin.domain.invoice.dto.CreateInvoiceDTO;
import com.juliano.meufin.domain.invoice.dto.CreateInvoiceResponseDTO;
import com.juliano.meufin.domain.invoice.dto.InvoicesListResponseDTO;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.wallet.Wallet;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.repository.WalletRepository;
import com.juliano.meufin.service.InvoiceService;
import com.juliano.meufin.util.AuthenticatedUser;
import com.juliano.meufin.util.StringToLocalDateTime;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

import java.util.UUID;

@RestController
@RequestMapping("invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final WalletRepository walletRepository;

    private final CategoryRepository categoryRepository;

    public InvoiceController(InvoiceService invoiceService, WalletRepository walletRepository, CategoryRepository categoryRepository) {
        this.invoiceService = invoiceService;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping

    public ResponseEntity<CreateInvoiceResponseDTO> create(@RequestBody @Valid CreateInvoiceDTO data, UriComponentsBuilder uriBuilder) {

        User user = AuthenticatedUser.get();

        Wallet wallet = walletRepository.getReferenceById(data.wallet_id());
        Category category = categoryRepository.getReferenceById(data.category_id());
        Invoice createdInvoice = this.invoiceService.create(new Invoice(
                data.name(),
                data.type(),
                data.value(),
                data.due_date(),
                user,
                wallet,
                category
        ));

        var uri = uriBuilder.cloneBuilder()
                .path("invoices/{id}")
                .buildAndExpand(createdInvoice.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new CreateInvoiceResponseDTO(createdInvoice));
    }

    @GetMapping
    public ResponseEntity<Page<InvoicesListResponseDTO>> list(Pageable pagination, String start_date, String end_date, String type, String status) {

        User user = AuthenticatedUser.get();


        LocalDateTime initialDate = StringToLocalDateTime.convert(start_date);
        LocalDateTime finalDate = StringToLocalDateTime.convert(end_date);
        var invoiceType = (type != null ? InvoiceTypes.valueOf(type.toUpperCase()) : null);
        var invoiceStatus = (status != null ? InvoiceStatus.valueOf(status.toUpperCase()) : null);
        var invoices = this.invoiceService.list(pagination, user, initialDate, finalDate,invoiceType,invoiceStatus);

        return ResponseEntity.ok(invoices.map(InvoicesListResponseDTO::new));

    }

    @GetMapping("/balance")
    public ResponseEntity<Balance> getBalance(String wallet_id) {

        User user = AuthenticatedUser.get();

        UUID WalletId = (wallet_id != null ? UUID.fromString(wallet_id) : null);


        Balance balance = this.invoiceService.getBalance(user, WalletId);

        return ResponseEntity.ok(balance);

    }
}
