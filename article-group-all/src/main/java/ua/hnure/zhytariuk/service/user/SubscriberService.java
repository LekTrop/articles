package ua.hnure.zhytariuk.service.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.hnure.zhytariuk.models.domain.user.User;
import ua.hnure.zhytariuk.models.domain.user.UserSubscriber;
import ua.hnure.zhytariuk.repo.user.UserSubscriberRepository;
import ua.hnure.zhytariuk.service.UserService;

@RequiredArgsConstructor
@Service
public class SubscriberService {

    @NonNull
    private final UserService userService;
    @NonNull
    private final UserSubscriberRepository userSubscribersRepository;

    public Boolean subscribeOrUnSubscribe(final String subscriberUsername, final String authorUsername) {
        final User subscriber = userService.loadUserByUsername(subscriberUsername);

        if (subscriber == null) {
            throw new RuntimeException();
        }

        final User user = userService.loadUserByUsername(authorUsername);

        if (user == null) {
            throw new RuntimeException();
        }

        final UserSubscriber userSubscriber =
                userSubscribersRepository.findBySubscriberUsernameAndUserUsername(subscriberUsername, authorUsername);

        if (userSubscriber == null) {
            userSubscribersRepository.save(UserSubscriber.builder()
                                                         .subscriber(subscriber)
                                                         .user(user)
                                                         .build());
            return true;
        } else {
            userSubscribersRepository.deleteById(userSubscriber.getUserSubscriberId());
            return false;
        }
    }

    public long subscribersCountByUsername(final String username) {
        return userSubscribersRepository.countByUserUsername(username);
    }
}
