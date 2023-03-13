package ua.hnure.zhytariuk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.article.ArticleModeration;

public interface ArticleModerationRepository extends JpaRepository<ArticleModeration, String> {
}
