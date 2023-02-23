package ua.hnure.zhytariuk.models.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "article_likes")
public class ArticleLike {

    @Id
    @UuidGenerator
    @Column(name = "article_like_id")
    private String articleLikeId;

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
