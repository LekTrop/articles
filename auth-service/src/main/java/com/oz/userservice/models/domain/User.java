package com.oz.userservice.models.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
@Builder(toBuilder = true)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_enabled")
    private Boolean isEnabled;
    @Column(name = "is_account_non_expired")
    private Boolean isAccountNonExpired;
    @Column(name = "is_credentials_non_expired")
    private Boolean isCredentialsNonExpired;
    @Column(name = "is_account_non_locked")
    private Boolean isAccountNonLocked;

    @ManyToOne
    @JoinColumn(name = "fk_role_id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @PrePersist
    public void prePersist() {
        if (isEnabled == null) {
            isEnabled = true;
        }
        if (isAccountNonExpired == null) {
            isAccountNonExpired = false;
        }
        if (isAccountNonLocked == null) {
            isAccountNonLocked = false;
        }
        if (isCredentialsNonExpired == null) {
            isCredentialsNonExpired = false;
        }
    }
}
