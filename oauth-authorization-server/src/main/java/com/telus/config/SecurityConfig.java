package com.telus.config;

import com.telus.filter.JwtAuthenticationFilter;
import com.telus.jwt.JwtAuthenticationEntryPoint;
import com.telus.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    public final UserInfoService userInfoService;

    @Autowired
    private JwtAuthenticationFilter filter;
    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Order(1)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/api/*"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->auth.anyRequest().authenticated())
                .userDetailsService(userInfoService)
                .httpBasic(withDefaults())
                .build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher("/h2-console/**"))
                .authorizeHttpRequests(auth ->auth.anyRequest().permitAll())
                .userDetailsService(userInfoService)
                .httpBasic(withDefaults())
                .build();
    }
    @Order(3)
    @Bean
    public SecurityFilterChain securityFilterChainAuthLogin(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeRequests().
                requestMatchers("/test").authenticated().requestMatchers("/auth/login").permitAll()
                .anyRequest()
                .authenticated()
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Order(4)
    @Bean
    public SecurityFilterChain h2ConsoleSecurityFilterChainConfig(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .securityMatcher(new AntPathRequestMatcher(("/h2-console/**")))
                .authorizeHttpRequests(auth->auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                // to display the h2Console in Iframe
                .headers(headers -> headers.frameOptions(withDefaults()).disable())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
