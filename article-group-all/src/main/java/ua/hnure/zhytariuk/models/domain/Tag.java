package ua.hnure.zhytariuk.models.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ua.hnure.zhytariuk.models.domain.article.Article;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @Column(name = "tag_id")
    private String tagId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "article_tags",
            inverseJoinColumns = @JoinColumn(name = "fk_article_id"),
            joinColumns = @JoinColumn(name = "fk_tag_id")
    )
    private Set<Article> articles =new HashSet<>();

    @PrePersist
    private void prePersist() {
        tagId = UUID.randomUUID()
                    .toString();
    }
}
