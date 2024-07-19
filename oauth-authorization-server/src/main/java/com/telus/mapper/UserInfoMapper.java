package com.telus.mapper;

import com.telus.dto.UserRegistrationDto;
import com.telus.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author atquil
 */
@Component
@RequiredArgsConstructor
public class UserInfoMapper {

    private final PasswordEncoder passwordEncoder;
    public UserInfo convertToEntity(UserRegistrationDto userRegistrationDto) {
        UserInfo userInfoEntity = new UserInfo();
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setEmail(userRegistrationDto.userEmail());
        userInfoEntity.setRoles(userRegistrationDto.userRoles());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        return userInfoEntity;
    }
}

