package com.oz.userservice.config;

import com.oz.userservice.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SpringSecurityConfiguration {

    @NonNull
    private final UserService userDetailsService;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeHttpRequests(request -> {
                    request.anyRequest().permitAll();
                })
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean("bCryptPasswordEncoder")
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean("daoAuthenticationProvider")
    public AuthenticationProvider daoAuthenticationProvider(){
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
