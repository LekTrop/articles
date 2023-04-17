package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.article.ArticleSaved;

import java.util.List;

public interface ArticleSavedRepository extends JpaRepository<ArticleSaved, String> {
    List<ArticleSaved> findAllByUserUsername(final String username);
    ArticleSaved findByUserUsernameAndArticleArticleId(final String username, final String articleId);
}
