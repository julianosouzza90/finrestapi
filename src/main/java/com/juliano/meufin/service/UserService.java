package com.juliano.meufin.service;

import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.user.dto.CreateUserDTO;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User create(CreateUserDTO data) throws ConflictException {
        if(this.userRepository.existsByLogin(data.login())) {
            throw new ConflictException("Já existe um usuário cadastrado com o email informado");
        }

        User user = new User(data);

        return this.userRepository.save(user);

    }

}
