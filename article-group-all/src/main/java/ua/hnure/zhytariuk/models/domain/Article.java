package ua.hnure.zhytariuk.models.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name = "price")
    private BigDecimal price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "fk_user_id", nullable = false)
    private User user;

    @Builder.Default
    @ManyToMany(mappedBy = "articles", fetch = FetchType.EAGER)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "fk_category_id", nullable = false)
    private Category category;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "article")
    private ArticleStatistic articleStatistic;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleLike> articleLikes = new ArrayList<>();

    public void addTags(final Set<Tag> tags) {
        this.tags = tags;

        tags.forEach(tag -> tag.getArticles()
                               .add(this));
    }

    public void addCategory(final Category category) {
        this.category = category;

        category.getArticles()
                .add(this);
    }

    public void addUser(final User user) {
        this.user = user;

        user.getArticles()
            .add(this);
    }

    public void addStatistic(final ArticleStatistic articleStatistic) {
        this.articleStatistic = articleStatistic;

        articleStatistic.setArticle(this);
    }
}
