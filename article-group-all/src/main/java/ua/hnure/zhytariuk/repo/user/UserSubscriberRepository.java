package ua.hnure.zhytariuk.repo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.hnure.zhytariuk.models.domain.user.Subscriber;

import java.util.List;

public interface UserSubscriberRepository extends JpaRepository<Subscriber, String> {
    Subscriber findBySubscriberUsernameAndUserUsername(String subscriberUsername, String authorUsername);

    @Query("SELECT s FROM Subscriber s WHERE s.user.username = :username " +
            "AND year(s.createdAt) = :year " +
            "AND month(s.createdAt) = :month")
    List<Subscriber> findAllByUserSubscriberUsernameAndYearAndMonth(String username,
                                                                    Integer month,
                                                                    Integer year);
}
