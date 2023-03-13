package ua.hnure.zhytariuk.repo.article;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleLikesRepository extends JpaRepository<ArticleLike, String> {

    ArticleLike findByUserUsernameAndArticleArticleId(String username, String articleId);

    @Query(
            value = "select ar.article_like_id, ar.created_at, ar.fk_user_id, ar.fk_article_id " +
                    "from article_likes ar " +
                    "inner join articles a on a.article_id = ar.fk_article_id " +
                    "inner join users u on a.fk_user_id = u.id " +
                    "where (:month IS NULL OR MONTH(ar.created_at) = :month) " +
                    "AND u.username = :username",
            nativeQuery = true
    )
    List<ArticleLike> findAllArticleLikesByUsernameAuthorAndMonth(final @Param("username") String username,
                                                                  final @Param("month") Integer month);
}
