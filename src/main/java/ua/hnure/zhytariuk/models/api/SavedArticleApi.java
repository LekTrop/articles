package ua.hnure.zhytariuk.models.api;

import lombok.*;
import ua.hnure.zhytariuk.models.domain.user.User;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class SavedArticleApi {

    private String id;
    private User user;
    private ArticleApi article;
}
