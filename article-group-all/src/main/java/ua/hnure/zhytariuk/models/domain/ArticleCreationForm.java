package ua.hnure.zhytariuk.models.domain;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ArticleCreationForm {
    private String titleImage;
    private String title;
    private String description;
    private String content;
    private String tags;
    private String categoryName;
    private BigDecimal price;
}
