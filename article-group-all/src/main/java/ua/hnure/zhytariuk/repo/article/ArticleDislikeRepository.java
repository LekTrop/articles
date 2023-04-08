package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.hnure.zhytariuk.models.domain.article.ArticleDislike;

import java.util.List;

public interface ArticleDislikeRepository extends JpaRepository<ArticleDislike, String> {
    ArticleDislike findByUserUsernameAndArticleArticleId(String username, String articleId);

    @Query("SELECT ad FROM ArticleDislike ad WHERE ad.article.user.username = :username " +
            "AND year(ad.createdAt)  = :year AND(month(ad.createdAt) = :month)"
    )
    List<ArticleDislike> findAllUserArticlesDislikesByYearAndMonth(final String username,
                                                                   final Integer year,
                                                                   final Integer month);
}

