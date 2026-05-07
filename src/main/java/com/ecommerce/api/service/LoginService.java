package com.ecommerce.api.service;

import com.ecommerce.api.dtos.user.LoginRequestDto;
import com.ecommerce.api.dtos.user.LoginResponseDto;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final TokenService tokenService;

    public LoginResponseDto authenticate(LoginRequestDto data) {
        UserEntity user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(data.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
        }

        user.updateLastLogin();
        userRepository.save(user);

        String token = tokenService.generateToken(user);

        return new LoginResponseDto("Login realizado com sucesso!", token);
    }

    public String validate(String token) {
        try {
            String email = tokenService.validateToken(token);
            if (email == null || email.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
            }
            return email;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido ou expirado");
        }
    }
}
