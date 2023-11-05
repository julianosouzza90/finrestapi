package com.juliano.meufin.domain.invoice;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.wallet.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "invoices")
@Table(name = "invoices")

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(name = "invoice_type")
    @Enumerated(EnumType.STRING)
    private InvoiceTypes type;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private BigDecimal value;

    @Column(name = "due_date")
    LocalDateTime dueDate;

    @ManyToOne
    @Lazy
    User user;

    @ManyToOne
    @Lazy
    private Wallet wallet;

    @ManyToOne
    @Lazy
    private Category category;





    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Invoice(
            String name,
            InvoiceTypes type,
            InvoiceStatus status,
            BigDecimal value,
            LocalDateTime dueDate,
            User user,
            Wallet wallet,
            Category category,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.value = value;
        this.dueDate = dueDate;
        this.user = user;
        this.wallet = wallet;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Invoice(String name, InvoiceTypes type, BigDecimal value, LocalDateTime dueDate, User user, Wallet wallet, Category category) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.user = user;
        this.dueDate = dueDate;
        this.wallet = wallet;
        this.category = category;
    }
}
