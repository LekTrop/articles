package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;

import java.util.List;

public interface ArticleLikesRepository extends JpaRepository<ArticleLike, String> {

    ArticleLike findByUserUsernameAndArticleArticleId(String username, String articleId);

    @Query("SELECT al FROM ArticleLike al WHERE al.article.user.username = :username " +
            "AND year(al.createdAt) = :year AND(month(al.createdAt) = :month)"
    )
    List<ArticleLike> findAllArticleLikesByUsernameAuthorAndYearAndMonth(final @Param("username") String username,
                                                                         final @Param("month") Integer month,
                                                                         final @Param("year") Integer year);
}
