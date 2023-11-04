package com.juliano.meufin.controller;
import com.juliano.meufin.domain.user.User;
import com.juliano.meufin.domain.user.dto.AuthResponseDTO;
import com.juliano.meufin.domain.user.dto.AuthUserDTO;
import com.juliano.meufin.domain.user.dto.CreateUserDTO;
import com.juliano.meufin.domain.user.dto.DetailedUserDTO;
import com.juliano.meufin.infra.exception.ConflictException;
import com.juliano.meufin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("auth")
public class AuthController {

    private UserService userService;


    public  AuthController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> doLogin(@RequestBody @Valid AuthUserDTO data) {
        String token = userService.login(data);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
    @PostMapping("/register")
    public ResponseEntity<DetailedUserDTO> createUser(@RequestBody @Valid CreateUserDTO data, UriComponentsBuilder uriBuilder) throws ConflictException {

        User createdUser = this.userService.create(data);
        var uri = uriBuilder.cloneBuilder()
                .path("users/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new DetailedUserDTO(createdUser));
    }
}
