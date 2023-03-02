package ua.hnure.zhytariuk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.user.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(final String name);
}
