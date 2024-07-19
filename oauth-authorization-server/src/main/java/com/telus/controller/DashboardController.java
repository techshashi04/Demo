package com.telus.controller;

import com.telus.entity.UserInfo;
import com.telus.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {
    @Autowired
    private UserInfoService userService;


    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN','ROLE_USER')")
    //@PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/welcome-message")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication){
        return ResponseEntity.ok("Welcome to the Oauth2:"+authentication.getName()+"with scope:"+authentication.getAuthorities());
    }

   @PreAuthorize("hasRole('ROLE_MANAGER')")
    //@PreAuthorize("hasAuthority('SCOPE_READ')")
    @GetMapping("/manager-message")
    public ResponseEntity<String> getManagerData(Principal principal){
        return ResponseEntity.ok("Manager::"+principal.getName());

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //@PreAuthorize("hasAuthority('SCOPE_WRITE')")
    @PostMapping("/admin-message")
    public ResponseEntity<String> getAdminData(@RequestParam("message") String message, Principal principal){
        return ResponseEntity.ok("Admin::"+principal.getName()+" has this message:"+message);

    }

    @PostMapping("/signup")
    public void signup(@RequestBody UserInfo user) {
        userService.saveNewUser(user);
    }
}