package ua.hnure.zhytariuk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.ArticleLike;

public interface ArticleLikesRepository extends JpaRepository<ArticleLike, String> {
    ArticleLike findByUserUsernameAndArticleArticleId(String username, String articleId);
}
