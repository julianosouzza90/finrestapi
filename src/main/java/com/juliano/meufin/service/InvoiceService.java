package com.juliano.meufin.service;

import com.juliano.meufin.domain.invoice.Balance;
import com.juliano.meufin.domain.invoice.Invoice;
import com.juliano.meufin.domain.invoice.InvoiceStatus;
import com.juliano.meufin.domain.invoice.InvoiceTypes;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.infra.exception.CreateInvoiceException;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.repository.InvoiceRepository;
import com.juliano.meufin.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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

    public Page<Invoice> list(Pageable pagination, User user, LocalDateTime initialDate, LocalDateTime finalDate, InvoiceTypes type, InvoiceStatus status) {

        if(initialDate != null && finalDate == null) {
            finalDate = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        }
        if(finalDate != null && initialDate == null) {
           throw  new CreateInvoiceException("Para filtrar por data, é preciso informar a data inicial");
        }

        if(initialDate != null) {

            if(type!= null && status!= null) {
                return this.invoiceRepository.findByUserIdAndStatusAndTypeAndCreatedAtBetween(
                        pagination,
                        user.getId(),
                        status,
                        type,
                        initialDate,
                        finalDate);
            }

            if(type != null) {
                return this.invoiceRepository.findByUserIdAndTypeAndCreatedAtBetween(
                       pagination,
                        user.getId(),
                        type,
                        initialDate, finalDate);
            }

            if(status!= null) {
                return this.invoiceRepository.findByUserIdAndStatusAndCreatedAtBetween(
                        pagination,
                        user.getId(),
                        status,
                        initialDate, finalDate);
            }

        }

        if(type != null &&status != null) {
            return  this.invoiceRepository.findByUserIdAndTypeAndStatus(pagination, user.getId(), type, status);
        }

        if(type != null) {
            return  this.invoiceRepository.findByUserIdAndType(pagination, user.getId(), type);
        }
        if(status != null) {
            return  this.invoiceRepository.findByUserIdAndStatus(pagination, user.getId(),status);
        }

        return this.invoiceRepository.findByUserId(pagination, user.getId());
    }
    public Balance getBalance(User user, UUID walletId) {

        List<Invoice> invoices = null;

        if(walletId != null) {
            invoices = this.invoiceRepository.getBalanceByUserIdAndWalletId(user.getId(), walletId);
        } else {
            invoices = this.invoiceRepository.getBalanceByUserId(user.getId());
        }


        return invoices.stream()
                .map(invoice -> {
                    Balance balance = new Balance();
                    if (invoice.getType().equals(InvoiceTypes.INCOME)) {
                        balance.setIncome(invoice.getValue());
                        return balance;
                    }
                    balance.setExpense(invoice.getValue());

                    return balance;
                }).reduce(new Balance(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), (acc, current) -> {
                    if(current.getIncome() != null) {
                        acc.setIncome(acc.getIncome().add(current.getIncome()));
                        acc.setTotal(acc.getTotal().add(current.getIncome()));
                    }
                    if(current.getExpense() != null) {
                        acc.setExpense(acc.getExpense().add(current.getExpense()));
                        acc.setTotal(acc.getTotal().subtract(current.getExpense()));
                    }

                    return acc;
                });



    }
}
