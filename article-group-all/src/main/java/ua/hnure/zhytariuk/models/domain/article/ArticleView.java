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
@Table(name = "article_view")
public class ArticleView {
    @Id
    @UuidGenerator
    @Column(name = "article_view_id")
    private String articleViewId;

    @OneToOne
    @JoinColumn(name = "fk_article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
