package com.telus.service;

import com.telus.config.UserInfoConfig;
import com.telus.entity.UserInfo;
import com.telus.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepository userInfoRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userInfoRepository.findByUserName(username)
                .map(UserInfoConfig::new).orElseThrow(() -> new  BadCredentialsException("Not found"));
    }

    public boolean saveNewUser(UserInfo user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // user.setRoles(Arrays.asList("USER"));
            userInfoRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("hahahhahhahahahah");
            return false;
        }
    }
}
