package ua.hnure.zhytariuk.models.domain.article;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ua.hnure.zhytariuk.models.domain.user.User;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "article_dislikes")
public class ArticleDislike {

    @Id
    @UuidGenerator
    @Column(name = "article_dislike_id")
    private String articleDislikeId;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_article_id")
    private Article article;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}