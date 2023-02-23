package ua.hnure.zhytariuk.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.hnure.zhytariuk.service.UserService;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

    @NonNull
    private final UserService userService;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) {
        httpSecurity.csrf()
                    .disable();

        httpSecurity.authorizeHttpRequests(request -> {
            request.anyRequest()
                   .permitAll();
        });

        httpSecurity.formLogin().loginPage("/login");

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);

        return daoAuthenticationProvider;
    }
}
