package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.article.ArticleView;

public interface ArticleViewRepository extends JpaRepository<ArticleView, String> {

    ArticleView findByUserUsernameAndArticleArticleId(String username, String articleId);

    long countByUserUsername(String name);
}
