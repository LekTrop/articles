package ua.hnure.zhytariuk.models.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import ua.hnure.zhytariuk.models.domain.Category;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "recommendations")
@Builder(toBuilder = true)
public class Recommendations {

    @Id
    @UuidGenerator
    @Column(name = "recommendation_id")
    private String recommendationId;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_category_id")
    private Category category;
}
