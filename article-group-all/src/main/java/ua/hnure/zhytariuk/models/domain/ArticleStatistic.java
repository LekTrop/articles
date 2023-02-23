package ua.hnure.zhytariuk.models.domain;

import jakarta.persistence.*;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "article_statistic")
public class ArticleStatistic {
    @Id
    @Column(name = "article_id")
    private String articleId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
}