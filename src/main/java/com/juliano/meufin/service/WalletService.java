package com.juliano.meufin.service;

import com.juliano.meufin.domain.category.Category;
import com.juliano.meufin.domain.wallet.Wallet;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.CategoryRepository;
import com.juliano.meufin.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet create(Wallet wallet) throws ConflictException {
        if(this.walletRepository.existsByUserIdAndName(wallet.getUser().getId(), wallet.getName())) {
             throw  new ConflictException("Já existe uma carteira cadastrada com o nome: " + wallet.getName());
        }

        return this.walletRepository.save(wallet);
    }
}
