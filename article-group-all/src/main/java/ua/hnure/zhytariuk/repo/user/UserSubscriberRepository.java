package ua.hnure.zhytariuk.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.user.Subscriber;

public interface UserSubscriberRepository extends JpaRepository<Subscriber, String> {
    Subscriber findBySubscriberUsernameAndUserUsername(String subscriberUsername, String authorUsername);

    long countByUserUsername(String username);
}
