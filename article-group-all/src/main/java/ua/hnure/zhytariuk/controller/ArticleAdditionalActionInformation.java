package ua.hnure.zhytariuk.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.hnure.zhytariuk.models.api.ArticleApi;

@Getter
@Setter
@Builder(toBuilder = true)
public class ArticleAdditionalActionInformation {
    private ArticleApi articleApi;
    private boolean isLike;
    private boolean isDislike;
    private boolean isSaved;
}
