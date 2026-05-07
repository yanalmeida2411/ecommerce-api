package com.ecommerce.api.mapper;

import com.ecommerce.api.dtos.user.UserResponseDto;
import com.ecommerce.api.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(UserEntity user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getBirthDate(),
                user.getGender(),
                user.getAddress()
        );
    }
}