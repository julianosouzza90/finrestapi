package com.juliano.meufin.domain.wallet;

import com.juliano.meufin.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "wallets")
@Table(name = "wallets")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @ManyToOne
    @Lazy
    private User user;

    @CreationTimestamp
    @Column(name  = "created_at")
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name  = "updated_at")
    private LocalDateTime updatedAt;

    public Wallet(String name,User user) {
        this.name = name;
        this.user = user;

    }



}