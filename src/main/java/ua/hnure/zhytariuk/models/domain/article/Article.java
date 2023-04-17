package ua.hnure.zhytariuk.models.domain.article;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ua.hnure.zhytariuk.models.domain.Category;
import ua.hnure.zhytariuk.models.domain.Tag;
import ua.hnure.zhytariuk.models.domain.user.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ua.hnure.zhytariuk.models.domain.article.ArticleStatus.ON_MODERATION;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "article_id")
    private String articleId;

    @Column(name = "title_image")
    private String titleImage;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @Builder.Default
    @ManyToMany(mappedBy = "articles", fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "fk_category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleSaved> savedArticles = new ArrayList<>();

    public void addTags(final Set<Tag> tags) {
        this.tags = tags;

        tags.forEach(tag -> tag.getArticles()
                               .add(this));
    }

    public void addCategory(final Category category) {
        this.category = category;

        if(category != null) {
            category.getArticles()
                    .add(this);
        }
    }

    public void addUser(final User user) {
        this.user = user;

        if(user != null) {
            user.getArticles()
                .add(this);
        }
    }

    @PrePersist
    private void prePersist() {

        this.status = ON_MODERATION;
    }
}
