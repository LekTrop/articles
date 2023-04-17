package ua.hnure.zhytariuk.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUsername(final String username);
}
