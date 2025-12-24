package com.poyraz.service;

import com.poyraz.dto.RegistrationResponseDTO;
import com.poyraz.dto.RegistrationUserDTO;
import com.poyraz.exceptions.UsernameAlreadyExistsException;

public interface UserService {

    RegistrationResponseDTO register(RegistrationUserDTO registrationUserDTO) throws UsernameAlreadyExistsException;
}
