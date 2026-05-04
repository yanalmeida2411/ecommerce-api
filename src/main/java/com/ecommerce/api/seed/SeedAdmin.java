package com.ecommerce.api.seed;

import com.ecommerce.api.enums.UserRole;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.UserRepository;
import lombok.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Builder
@Configuration
public class SeedAdmin {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin@email.com").isEmpty()) {

                UserEntity admin = UserEntity.builder()
                        .firstName("Administrador")
                        .lastName("1")
                        .email("admin@email.com")
                        .password(passwordEncoder.encode("admin123"))
                        .phone("9999999999")
                        .birthDate(LocalDate.of(2000, 11, 24))
                        .gender("Masculino")
                        .address("Rua Fernando de Azevedo")
                        .role(UserRole.ADMIN)
                        .build();

                userRepository.save(admin);
                System.out.println("Usuário Admin criado com sucesso!");
            }
        };
    }
}
