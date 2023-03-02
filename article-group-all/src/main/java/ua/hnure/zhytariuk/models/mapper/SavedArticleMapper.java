package ua.hnure.zhytariuk.models.mapper;

import org.mapstruct.Mapper;
import ua.hnure.zhytariuk.models.domain.article.ArticleSaved;
import ua.hnure.zhytariuk.models.api.SavedArticleApi;
import ua.hnure.zhytariuk.models.mapper.config.MapStructConfig;

@Mapper(
        config = MapStructConfig.class,
        uses = {
                ArticleMapper.class
        }
)
public interface SavedArticleMapper {
    ArticleSaved toDomain(final SavedArticleApi savedArticleApi);

    SavedArticleApi toApi(final ArticleSaved articleSaved);
}
