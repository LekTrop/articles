package ua.hnure.zhytariuk.repo.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hnure.zhytariuk.models.domain.article.Article;

import java.math.BigDecimal;

public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query(
            "FROM Article a " +
            "         INNER JOIN a.category c " +
            "         INNER JOIN a.user u " +
            "WHERE (:username IS NULL OR :username = u.username) " +
            "AND (:categoryName IS NULL OR :categoryName LIKE '' OR c.name = :categoryName) " +
            "AND (:maxPrice IS NULL OR a.price <= :maxPrice) " +
            "AND (:minPrice IS NULL OR a.price >= :minPrice)"
    )
    Page<Article> findAllWithFilters(
            final @Param("username") String username,
            final @Param("categoryName") String categoryName,
            final @Param("maxPrice") BigDecimal maxPrice,
            final @Param("minPrice") BigDecimal minPrice,
            final Pageable pageable);
}
