package ua.hnure.zhytariuk.models.domain.article;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ua.hnure.zhytariuk.models.domain.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "article_moderation")
public class ArticleModeration {
    @Id
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ModerationStatus status;

    @Column(name = "additional_information")
    private String additionalInformation;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User moderator;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Article article;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist(){
        this.status = ModerationStatus.NOT_ASSIGNED;
    }
}
