package ua.hnure.zhytariuk.models.domain.article;

import jakarta.persistence.*;
import lombok.*;
import ua.hnure.zhytariuk.models.domain.user.User;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "saved_articles")
public class ArticleSaved {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "fk_username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_article_id", nullable = false)
    private Article article;
}
