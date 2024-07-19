package com.telus.config;

import com.telus.entity.UserInfo;
import com.telus.repo.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitialUserInfo  implements CommandLineRunner {
    private final UserInfoRepository userInfoRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        UserInfo manager = new UserInfo();
        manager.setUserName("Manager");
        manager.setPassword(passwordEncoder.encode("password"));
        manager.setRoles(List.of("ROLE_MANAGER"));

        UserInfo admin = new UserInfo();
        admin.setUserName("Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles(List.of("ROLE_ADMIN"));

        UserInfo user = new UserInfo();
        user.setUserName("User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(List.of("ROLE_USER"));

        userInfoRepo.saveAll(List.of(manager,admin,user));
        System.out.println("User created from cmd");
    }
}
