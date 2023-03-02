package ua.hnure.zhytariuk.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.hnure.zhytariuk.service.user.SubscriberService;

@RestController
@RequestMapping("api/v1/subscribers")
@RequiredArgsConstructor
public class SubscriberController {

    @NonNull
    private final SubscriberService subscriberService;

    @PostMapping("subscribe/{subscriberUsername}/{authorUsername}")
    public ResponseEntity<?> subscribe(
            final @PathVariable String subscriberUsername,
            final @PathVariable String authorUsername
    ) {
        final Boolean isSubscribed =
                subscriberService.subscribeOrUnSubscribe(subscriberUsername, authorUsername);

        if (isSubscribed) {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.ACCEPTED);
        }
    }
}
