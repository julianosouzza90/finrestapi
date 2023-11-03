package com.juliano.meufin.repository;

import com.juliano.meufin.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByLogin(String login);

    UserDetails findByLogin(String username);

    UserDetails getUserById(UUID uuid);

    boolean existsById(UUID id);
}
