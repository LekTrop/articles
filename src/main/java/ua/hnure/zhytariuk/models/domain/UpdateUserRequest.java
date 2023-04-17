package ua.hnure.zhytariuk.models.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UpdateUserRequest {
    private String username;
    private String email;
    private String imageSrc;
    private String description;

    private String oldPassword;
    private String newPassword;
    private String repeatPassword;
}
