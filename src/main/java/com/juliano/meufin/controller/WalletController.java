package com.juliano.meufin.controller;

import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.wallet.Wallet;
import com.juliano.meufin.domain.wallet.dto.CreateWalletDTO;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.WalletRepository;
import com.juliano.meufin.service.WalletService;
import com.juliano.meufin.util.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("wallets")
public class WalletController {

    private final WalletService walletService;
    private final WalletRepository walletRepository;

    public WalletController(WalletService walletService, WalletRepository walletRepository) {
        this.walletService = walletService;
        this.walletRepository = walletRepository;
    }

    @PostMapping
    public ResponseEntity<WalletDetailsDTO> create(@RequestBody @Valid CreateWalletDTO data, UriComponentsBuilder uriBuilder) throws ConflictException {
        User user = AuthenticatedUser.get();
        System.out.println(user);
        Wallet createdWallet = this.walletService.create(new Wallet(data.name(), user));

        var uri = uriBuilder.cloneBuilder()
                .path("wallets/{id}")
                .buildAndExpand(createdWallet.getId())
                .toUri();


        return ResponseEntity.created(uri).body(new WalletDetailsDTO(createdWallet));
    }
    @GetMapping
    public ResponseEntity<Page<WalletDetailsDTO>> listAll(Pageable pagination) {
        User user = AuthenticatedUser.get();
        Page<Wallet> wallets = this.walletRepository.findByUserId(pagination, user.getId());

        return ResponseEntity.ok(wallets.map(WalletDetailsDTO::new));
    }
}
