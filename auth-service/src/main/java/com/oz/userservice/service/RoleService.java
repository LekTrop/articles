package com.oz.userservice.service;

import com.oz.userservice.models.domain.Role;
import com.oz.userservice.repo.RoleRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    @NonNull
    private final RoleRepository roleRepository;

    public Optional<Role> findByName(final String name) {
        return roleRepository.findRoleByName(name);
    }
}
