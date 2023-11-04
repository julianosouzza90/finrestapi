package com.juliano.meufin.domain.category;

import com.juliano.meufin.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.rmi.server.UID;
import java.time.LocalDateTime;

@Entity(name = "categories")
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UID id;

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


    public Category(String categoryName, User user) {
        this.name = categoryName;
        this.user = user;
    }

    public Category(String name) {
        this.name = name;
    }
}
