package ua.hnure.zhytariuk.models.api;

import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class RoleApi {
    private String id;
    private String name;
}
