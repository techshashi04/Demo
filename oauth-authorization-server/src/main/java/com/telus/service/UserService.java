package com.telus.service;

import com.telus.entity.UserInfo;
import com.telus.repo.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserInfoRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public void saveAdmin(UserInfo user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
       // user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepository.save(user);
    }

    public void saveUser(UserInfo user) {
        userRepository.save(user);
    }

    public List<UserInfo> getAll() {
        return userRepository.findAll();
    }

    public Optional<UserInfo> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public UserInfo findByUserName(String userName) {
        return userRepository.findByUserName(userName).get();
    }
}
