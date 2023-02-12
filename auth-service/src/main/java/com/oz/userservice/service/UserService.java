package com.oz.userservice.service;

import com.oz.userservice.models.domain.Role;
import com.oz.userservice.models.domain.User;
import com.oz.userservice.repo.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Value("${role.default}")
    private String defaultRole;

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(@NonNull RoleService roleService,
                       @NonNull UserRepository userRepository,
                       @NonNull @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow();
    }

    public User save(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        final Role role = roleService.findByName(user.getRole().getName())
                                     .orElse(roleService.findByName(defaultRole)
                                                        .orElseThrow());

        user.setRole(role);

        return userRepository.save(user);
    }
}
