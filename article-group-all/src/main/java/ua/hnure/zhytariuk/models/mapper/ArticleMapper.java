package ua.hnure.zhytariuk.models.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ua.hnure.zhytariuk.models.api.ArticleApi;
import ua.hnure.zhytariuk.models.domain.article.Article;
import ua.hnure.zhytariuk.models.domain.article.ArticleCreationForm;
import ua.hnure.zhytariuk.models.domain.Category;
import ua.hnure.zhytariuk.models.domain.Tag;
import ua.hnure.zhytariuk.models.mapper.config.MapStructConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class)
public interface ArticleMapper {
    Article toDomain(final ArticleApi articleApi);

    @Mapping(target = "tagNames", qualifiedByName = "tagToTagNames", source = "tags")
    @Mapping(target = "categoryName", qualifiedByName = "getCategoryName", source = "category")
    ArticleApi toApi(final Article article);

    @Mapping(target = "tags", ignore = true)
    Article toDomain(final ArticleCreationForm articleCreationForm);

    @Named(value = "tagToTagNames")
    default List<String> getTagNames(final Set<Tag> tags) {
        return tags.stream()
                   .map(Tag::getName)
                   .collect(Collectors.toList());
    }

    @Named(value = "getCategoryName")
    default String getCategoryName(final Category category) {
        return category.getName();
    }
}
