package ua.hnure.zhytariuk.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.hnure.zhytariuk.models.domain.Tag;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, String> {
    @Query("SELECT t FROM Tag t WHERE t.name in :names")
    Set<Tag> getTagsInNames(final @Param("names") Set<String> names);
}
