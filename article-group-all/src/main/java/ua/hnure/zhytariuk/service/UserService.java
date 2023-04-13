package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.hnure.zhytariuk.models.domain.UpdateUserRequest;
import ua.hnure.zhytariuk.models.domain.user.Role;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.repo.UserRepository;

import java.util.Objects;
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
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                             .orElseThrow(() -> new UsernameNotFoundException("User was not found"));
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

    public User update(final UpdateUserRequest updateRequest, final User user) {

        final User updated = updateIfNotEqualsWithExisted(updateRequest, user);

        return userRepository.save(updated);
    }

    private User updateIfNotEqualsWithExisted(final UpdateUserRequest updateUserRequest, final User existed) {

        User.UserBuilder userBuilder = existed.toBuilder();

        if (!StringUtils.isBlank(updateUserRequest.getUsername())) {
            if (!Objects.equals(updateUserRequest.getUsername(), existed.getUsername())) {
                userBuilder.username(updateUserRequest.getUsername());
            }
        }

        if (!StringUtils.isBlank(updateUserRequest.getEmail())) {
            if (!Objects.equals(updateUserRequest.getEmail(), existed.getEmail())) {
                userBuilder.email(updateUserRequest.getEmail());
            }
        }

        if (!Objects.equals(updateUserRequest.getDescription(), existed.getDescription())) {
            userBuilder.description(updateUserRequest.getDescription());
        }

        if (!StringUtils.isBlank(updateUserRequest.getNewPassword())) {
            if (!passwordEncoder.matches(updateUserRequest.getNewPassword(), existed.getPassword())) {
                userBuilder.password(passwordEncoder.encode(updateUserRequest.getNewPassword()));
            }
        }

        return userBuilder.build();
    }
}
