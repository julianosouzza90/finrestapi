package com.juliano.meufin.repository;

import com.juliano.meufin.domain.wallet.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    boolean existsByUserIdAndName(UUID userId, String name);

    Page<Wallet> findByUserId(Pageable pagination, UUID id);

    boolean existsByIdAndUserId(UUID id, UUID userId);
}
