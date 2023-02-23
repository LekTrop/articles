package ua.hnure.zhytariuk.models.api;

import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class TagApi {
    private String tagId;
    private String name;
    private LocalDateTime createdAt;
}
