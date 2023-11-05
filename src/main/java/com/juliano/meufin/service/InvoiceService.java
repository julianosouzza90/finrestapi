package com.juliano.meufin.service;

import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.dto.ListInvoicesDTO;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.infra.exception.CreateInvoiceException;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.repository.InvoiceRepository;
import com.juliano.meufin.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class InvoiceService {

    private final CategoryRepository categoryRepository;
    private final WalletRepository walletRepository;

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(CategoryRepository categoryRepository, WalletRepository walletRepository, InvoiceRepository invoiceRepository) {
        this.categoryRepository = categoryRepository;
        this.walletRepository = walletRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice create(Invoice invoice) {
        if(!this.walletRepository.existsByIdAndUserId(invoice.getWallet().getId(), invoice.getUser().getId())) {
            throw  new CreateInvoiceException("A carteira informada é inválida ou inexistente!");
        }

       if(!this.categoryRepository.existsByIdAndUserId(invoice.getCategory().getId(), invoice.getUser().getId())) {
           throw  new CreateInvoiceException("A categoria informada é inválida ou inexistente!");
       }

       return this.invoiceRepository.save(invoice);
    }

    public Page<Invoice> list(ListInvoicesDTO data, User user) {
        LocalDateTime initialDate = data.startDate();
        LocalDateTime finalDate = data.endDate();

        if(initialDate != null && finalDate == null) {
            finalDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        }
        if(finalDate != null && initialDate == null) {
            initialDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        }

        if(initialDate != null) {

            if(data.type() != null && data.status() != null) {
                return this.invoiceRepository.findByUserIdAndStatusAndTypeAndCreatedAtBetween(
                        data.pagination(),
                        user.getId(),
                        data.status(),
                        data.type(),
                        initialDate, finalDate);
            }

            if(data.type() != null) {
                return this.invoiceRepository.findByUserIdAndTypeAndCreatedAtBetween(
                        data.pagination(),
                        user.getId(),
                        data.type(),
                        initialDate, finalDate);
            }

            if(data.status() != null) {
                return this.invoiceRepository.findByUserIdAndStatusAndCreatedAtBetween(
                        data.pagination(),
                        user.getId(),
                        data.status(),
                        initialDate, finalDate);
            }

        }

        if(data.type() != null && data.status() != null) {
            return  this.invoiceRepository.findByUserIdAndTypeAndStatus(data.pagination(), user.getId(), data.type(), data.status());
        }

        if(data.type() != null) {
            return  this.invoiceRepository.findByUserIdAndType(data.pagination(), user.getId(), data.type());
        }
        if(data.status() != null) {
            return  this.invoiceRepository.findByUserIdAndStatus(data.pagination(), user.getId(), data.status());
        }

        return this.invoiceRepository.findByUserId(data.pagination(), user.getId());
    }
}
