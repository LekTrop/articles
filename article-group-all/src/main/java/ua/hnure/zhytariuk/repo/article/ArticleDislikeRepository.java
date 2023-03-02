package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.article.ArticleDislike;

import java.util.List;

public interface ArticleDislikeRepository extends JpaRepository<ArticleDislike, String> {
    ArticleDislike findByUserUsernameAndArticleArticleId(String username, String articleId);

    List<ArticleDislike> findAllByUserUsername(final String username);
}

