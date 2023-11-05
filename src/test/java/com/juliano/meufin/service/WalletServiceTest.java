package com.juliano.meufin.service;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.wallet.Wallet;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceTest {

    @Mock
    WalletRepository walletRepository;

    @InjectMocks
    WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("Should throw an exception, when the wallet name is already in use for the user")
    void CreateWalletCase1() {
        Wallet wallet = new Wallet("Wallet Test", new User());

        when(walletRepository.existsByUserIdAndName(wallet.getUser().getId(), wallet.getName())).thenReturn(true);

        var exception = assertThrows(ConflictException.class, () -> {
           walletService.create(wallet);
        });

        assertEquals(exception.getMessage(), "Já existe uma carteira cadastrada com o nome: " + wallet.getName());

    }

    @Test
    @DisplayName("Should create a new Wallet")
    void CreateWalletCase2() throws ConflictException {
        Wallet wallet = new Wallet("Wallet Test", new User());

        when(walletRepository.existsByUserIdAndName(wallet.getUser().getId(), wallet.getName())).thenReturn(false);
        when(walletRepository.save(wallet)).thenReturn(wallet);
        Wallet result = walletService.create(wallet);

        assertNotNull(result);
        assertEquals(result.getName(), wallet.getName());
        verify(walletRepository, times(1)).existsByUserIdAndName(any(), any());

    }
}