package com.oz.userservice.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class UserApi {
    private String id;
    private String username;
    private String password;
    private String email;
    private Boolean isEnabled;
    private Boolean isAccountNonExpired;
    private Boolean isCredentialsNonExpired;
    private Boolean isAccountNonLocked;

    @JsonProperty("role")
    private RoleApi role;
}
