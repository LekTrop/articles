package ua.hnure.zhytariuk.models.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_subscribers")
@Builder(toBuilder = true)
public class UserSubscriber {

    @Id
    @UuidGenerator
    @Column(name = "user_subscriber_id")
    private String userSubscriberId;

    @ManyToOne
    @JoinColumn(name = "fk_subscriber_id")
    private User subscriber;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
