package ua.hnure.zhytariuk.models.api;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ArticleApi {
    private String articleId;

    private String titleImage;
    private String title;
    private String description;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<String> tagNames;
    private String categoryName;
}
