package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query(
            "FROM Article a " +
                    "         INNER JOIN a.category c " +
                    "         INNER JOIN a.user u " +
                    "WHERE (:username IS NULL OR :username LIKE '' OR :username = u.username) " +
                    "AND (:categoryName IS NULL OR :categoryName LIKE '' OR c.name = :categoryName) " +
                    "AND (:title IS NULL OR :title LIKE '' OR a.title LIKE %:title%) " +
                    "AND (:startDate IS NULL OR a.createdAt > :startDate) " +
                    "AND (:endDate IS NULL OR a.createdAt < :endDate) " +
                    "AND (:status IS NULL OR a.status = :status)"
    )
    Page<Article> findAllWithFilters(
            final @Param("username") String username,
            final @Param("categoryName") String categoryName,
            final @Param("title") String title,
            final @Param("startDate") LocalDate startDate,
            final @Param("endDate") LocalDate endDate,
            final @Param("status") ArticleStatus status,
            final Pageable pageable);
}
