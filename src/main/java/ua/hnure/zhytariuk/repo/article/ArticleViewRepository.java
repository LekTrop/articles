package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.hnure.zhytariuk.models.domain.article.ArticleView;

import java.util.Collection;
import java.util.List;

public interface ArticleViewRepository extends JpaRepository<ArticleView, String> {

    ArticleView findByUserUsernameAndArticleArticleId(String username, String articleId);

    long countByUserUsername(String name);

    List<ArticleView> findAllByArticleUserUsername(String username);

    @Query(
            "SELECT av FROM ArticleView av WHERE av.article.user.username = :username " +
                    "AND year(av.createdAt) = :year " +
                    "AND month(av.createdAt) = :month"

    )
    List<ArticleView> findAllArticleLikesByUsernameAuthorAndMonthAndYear(final String username,
                                                                         final Integer month,
                                                                         final Integer year);
}
