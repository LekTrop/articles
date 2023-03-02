package ua.hnure.zhytariuk.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.user.UserSubscriber;

public interface UserSubscriberRepository extends JpaRepository<UserSubscriber, String> {
    UserSubscriber findBySubscriberUsernameAndUserUsername(String subscriberUsername, String authorUsername);

    long countByUserUsername(String username);
}
