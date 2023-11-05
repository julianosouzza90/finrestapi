package com.juliano.meufin.controller;

import com.juliano.meufin.domain.wallet.Wallet;

import java.time.LocalDateTime;
import java.util.UUID;

public record WalletDetailsDTO(UUID id, String name, LocalDateTime created_at) {
    public WalletDetailsDTO(Wallet wallet) {
        this(wallet.getId(), wallet.getName(), wallet.getCreatedAt());
    }}
