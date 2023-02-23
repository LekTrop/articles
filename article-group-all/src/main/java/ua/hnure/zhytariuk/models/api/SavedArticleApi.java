package ua.hnure.zhytariuk.models.api;

import lombok.*;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SavedArticleApi {

    private String id;
    private String userId;
    private ArticleApi article;
}
