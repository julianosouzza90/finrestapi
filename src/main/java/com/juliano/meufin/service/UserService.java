package com.juliano.meufin.service;

import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.user.dto.AuthUserDTO;
import com.juliano.meufin.domain.user.dto.CreateUserDTO;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.infra.security.TokenService;
import com.juliano.meufin.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthenticationManager authManager;

    private TokenService tokenService;

    public UserService(UserRepository userRepository, AuthenticationManager authManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }



    public User create(CreateUserDTO data) throws ConflictException {
        if(this.userRepository.existsByLogin(data.login())) {
            throw new ConflictException("Já existe um usuário cadastrado com o email informado");
        }

        User user = new User(data);

        return this.userRepository.save(user);

    }

    public String login(AuthUserDTO data) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authenticate = this.authManager.authenticate(usernameAndPassword);

        return  this.tokenService.generate((User) authenticate.getPrincipal());
    }
}
