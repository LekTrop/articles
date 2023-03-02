package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.article.ArticleLike;

import java.util.List;

public interface ArticleLikesRepository extends JpaRepository<ArticleLike, String> {
    ArticleLike findByUserUsernameAndArticleArticleId(String username, String articleId);

    List<ArticleLike> findAllByUserUsername(final String username);
}
