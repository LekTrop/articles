package ua.hnure.zhytariuk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.hnure.zhytariuk.models.domain.SavedArticle;

import java.util.List;

public interface SavedArticleRepository extends JpaRepository<SavedArticle, String> {
    List<SavedArticle> findAllByUserId(final String userId);
}
