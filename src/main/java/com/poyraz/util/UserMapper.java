package com.poyraz.util;

import com.poyraz.dto.RegistrationResponseDTO;
import com.poyraz.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    RegistrationResponseDTO savedUserToRegistrationResponseDTO(User user);
}
