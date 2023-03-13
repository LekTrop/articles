package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.user.Role;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Value("${default.role}")
    private String defaultRole;

    @NonNull
    private final RoleService roleService;
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final PasswordEncoder passwordEncoder;

    public UserService(@NonNull RoleService roleService,
                       @NonNull UserRepository userRepository,
                       @NonNull @Lazy @Qualifier("bCryptPasswordEncoder") PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                             .orElseThrow();
    }



    public User save(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        final String roleName = Optional.ofNullable(user.getRole())
                                        .map(Role::getName)
                                        .orElse(defaultRole);

        final Role role = roleService.findByName(roleName)
                                     .orElseThrow();

        user.setRole(role);

        return userRepository.save(user);
    }
}
