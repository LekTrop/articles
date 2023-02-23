package ua.hnure.zhytariuk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.hnure.zhytariuk.models.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.name FROM Category c")
    List<String> findAllCategoryNames();

    Optional<Category> findByName(String name);
}
