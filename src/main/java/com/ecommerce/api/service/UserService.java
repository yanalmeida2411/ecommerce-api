package com.ecommerce.api.service;

import com.ecommerce.api.dtos.user.UserRequestDto;
import com.ecommerce.api.dtos.user.UserResponseDto;
import com.ecommerce.api.enums.UserRole;
import com.ecommerce.api.mapper.UserMapper;
import com.ecommerce.api.model.UserEntity;
import com.ecommerce.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() != UserRole.ADMIN)
                .map(userMapper::toResponseDto)
                .toList();
    }

    public UserResponseDto findUserById(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
        );
        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createUser(UserRequestDto userDto) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(userDto.email());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado.");
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .email(userDto.email())
                .password(passwordEncoder.encode(userDto.password()))
                .phone(userDto.phone())
                .birthDate(userDto.birthDate())
                .gender(userDto.gender())
                .address(userDto.address())
                .build();

        UserEntity savedUser = userRepository.save(newUser);
        return userMapper.toResponseDto(savedUser);
    }

    @Transactional
    public UserResponseDto updatingUser(UUID userId, UserRequestDto dto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            userRepository.findByEmail(dto.email()).ifPresent(u -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
            });
        }

        user.updateProfile(dto);

        if (dto.password() != null && !dto.password().isBlank()) {
            user.updatePassword(passwordEncoder.encode(dto.password()));
        }

        return userMapper.toResponseDto(userRepository.save(user));
    }

    public void deleteUserById(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        userRepository.deleteById(userId);
    }
}