package ua.hnure.zhytariuk.models.domain;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ArticleSearchFilterForm {
    private String categoryName;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Integer page;
}
