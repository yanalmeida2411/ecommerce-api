package com.ecommerce.api.utils;

import com.ecommerce.api.enums.UserRole;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class GetAuthenticatedUser {

    private final UserRepository userRepository;

    public UserEntity getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public void validateAdminRole() {
        UserEntity user = getAuthenticatedUser();
        if (user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: Esta operação exige privilégios de administrador.");
        }
    }
}
