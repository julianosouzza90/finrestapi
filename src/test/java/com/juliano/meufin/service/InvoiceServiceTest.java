package com.juliano.meufin.service;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;
import com.juliano.meufin.domain.invoice.dto.ListInvoicesDTO;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.wallet.Wallet;
import com.juliano.meufin.infra.exception.CreateInvoiceException;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.repository.InvoiceRepository;
import com.juliano.meufin.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InvoiceServiceTest {
    @Mock
    private  CategoryRepository categoryRepository;
    @Mock
    private  WalletRepository walletRepository;
    @Mock
    private  InvoiceRepository invoiceRepository;
    @InjectMocks
    private InvoiceService invoiceService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);;
    }

    @Test
    @DisplayName("Should throw CreateInvoiceException when the wallet is invalid.")
    void createInvoiceCase1() {
        Invoice invoice = new Invoice(
                "invoice Test",
                InvoiceTypes.INCOME,
                InvoiceStatus.PENDING,
                BigDecimal.valueOf(150),
                LocalDateTime.now().plusDays(1),
                new User(),
                new Wallet(),
                new Category(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(walletRepository.existsByIdAndUserId(UUID.randomUUID(), UUID.randomUUID())).thenReturn(false);
        when(categoryRepository.existsByIdAndUserId(any(), any())).thenReturn(true);
        var exception = assertThrows(CreateInvoiceException.class, () -> {
            invoiceService.create(invoice);
        });

        assertEquals("A carteira informada é inválida ou inexistente!",exception.getMessage());
    }

    @Test
    @DisplayName("Should throw CreateInvoiceException when the category is invalid.")
    void createInvoiceCase2() {
        Invoice invoice = new Invoice(
                "invoice Test",
                InvoiceTypes.INCOME,
                InvoiceStatus.PENDING,
                BigDecimal.valueOf(150),
                LocalDateTime.now().plusDays(1),
                new User(),
                new Wallet(),
                new Category(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(walletRepository.existsByIdAndUserId(any(), any())).thenReturn(true);
        when(categoryRepository.existsByIdAndUserId(any(), any())).thenReturn(false);
        var exception = assertThrows(CreateInvoiceException.class, () -> {
            invoiceService.create(invoice);
        });

        assertEquals( "A categoria informada é inválida ou inexistente!",exception.getMessage());
    }

    @Test
    @DisplayName("Should create a new invoice")
    void createInvoiceCase3() {
        Invoice invoice = new Invoice(
                "invoice Test",
                InvoiceTypes.INCOME,
                InvoiceStatus.PENDING,
                BigDecimal.valueOf(150),
                LocalDateTime.now().plusDays(1),
                new User(),
                new Wallet(),
                new Category(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(walletRepository.existsByIdAndUserId(any(), any())).thenReturn(true);
        when(categoryRepository.existsByIdAndUserId(any(), any())).thenReturn(true);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice result = invoiceService.create(invoice);

        assertEquals("invoice Test", result.getName());

        verify(invoiceRepository, times(1)).save(invoice);
    }


    @Test
    @DisplayName("Should filter using all filter options")
    void ListInvoiceCase1() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserIdAndStatusAndTypeAndCreatedAtBetween(any(), any(), any(), any(), any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable, startDate,endDate,InvoiceTypes.INCOME, InvoiceStatus.PENDING ), new User());


        assertEquals(3, result.getSize());

        verify(invoiceRepository, times(1)).findByUserIdAndStatusAndTypeAndCreatedAtBetween(any(), any(), any(), any(), any(), any());

    }

    @Test
    @DisplayName("Should filter using invoice type and createdAt between")
    void ListInvoiceCase2() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserIdAndTypeAndCreatedAtBetween(any(), any(), any(), any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable, startDate,endDate,InvoiceTypes.INCOME, null ), new User());

        assertEquals(3, result.getSize());


        verify(invoiceRepository, times(1)).findByUserIdAndTypeAndCreatedAtBetween(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should filter using invoice status and createdAt between")
    void ListInvoiceCase3() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserIdAndStatusAndCreatedAtBetween(any(), any(), any(), any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable, startDate,endDate,null, InvoiceStatus.PENDING ), new User());

        assertEquals(3, result.getSize());


        verify(invoiceRepository, times(1)).findByUserIdAndStatusAndCreatedAtBetween(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should filter using invoice status and createdAt between")
    void ListInvoiceCase4() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserIdAndTypeAndStatus(any(), any(), any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable, null,null,InvoiceTypes.INCOME, InvoiceStatus.PENDING ), new User());

        assertEquals(3, result.getSize());


        verify(invoiceRepository, times(1)).findByUserIdAndTypeAndStatus(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should filter using invoice type")
    void ListInvoiceCase5() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserIdAndType(any(), any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable, null,null,InvoiceTypes.INCOME, null ), new User());

        assertEquals(3, result.getSize());


        verify(invoiceRepository, times(1)).findByUserIdAndType(any(), any(), any());
    }

    @Test
    @DisplayName("Should filter using invoice status")
    void ListInvoiceCase6() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserIdAndStatus(any(), any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable,  null,null,null, InvoiceStatus.PENDING ), new User());

        assertEquals(3, result.getSize());


        verify(invoiceRepository, times(1)).findByUserIdAndStatus(any(), any(), any());
    }
    @Test
    @DisplayName("Should list all without filter")
    void ListInvoiceCase7() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.of(2023, 10,1), LocalTime.now());
        LocalDateTime endDate = LocalDateTime.of(LocalDate.of(2023, 11,1), LocalTime.now());

        List<Invoice> invoicesList = createListInvoices(startDate, endDate);
        Pageable pageable  = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(invoicesList, pageable, invoicesList.size());


        when(invoiceRepository.findByUserId(any(), any())).thenReturn(page);

        Page<Invoice> result  = this.invoiceService.list(new ListInvoicesDTO(pageable, null,null,null, null ), new User());

        assertEquals(3, result.getSize());


        verify(invoiceRepository, times(1)).findByUserId(any(), any());
    }

    private List<Invoice> createListInvoices(LocalDateTime startDate, LocalDateTime endDate) {

        var invoice1 = new Invoice();
        invoice1.setCreatedAt(startDate);

        var invoice2 = new Invoice();
        invoice2.setCreatedAt(endDate);

        var invoice3 = new Invoice();
        invoice3.setCreatedAt(startDate.minusMonths(1));


        return  List.of(invoice1,invoice2, invoice3);

    }
}