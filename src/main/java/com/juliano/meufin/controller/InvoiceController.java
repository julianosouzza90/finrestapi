package com.juliano.meufin.controller;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.dto.CreateInvoiceDTO;
import com.juliano.meufin.domain.invoice.dto.CreateInvoiceResponseDTO;
import com.juliano.meufin.domain.invoice.dto.InvoicesListResponseDTO;
import com.juliano.meufin.domain.invoice.dto.ListInvoicesDTO;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.wallet.Wallet;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.repository.WalletRepository;
import com.juliano.meufin.service.InvoiceService;
import com.juliano.meufin.util.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<Page<InvoicesListResponseDTO>> list(ListInvoicesDTO data) {

        User user = AuthenticatedUser.get();
        var invoices = this.invoiceService.list(data, user);

        return ResponseEntity.ok(invoices.map(InvoicesListResponseDTO::new));

    }
}
