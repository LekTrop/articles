package ua.hnure.zhytariuk.models.domain.article;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ArticleSearchFilterForm {
    private String categoryName;
    private String username;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer page;
    private Integer paginationSize;
}
