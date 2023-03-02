package ua.hnure.zhytariuk.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.user.Role;
import ua.hnure.zhytariuk.repo.RoleRepository;

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
