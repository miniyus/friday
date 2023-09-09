package com.miniyus.friday.infrastructure.auth.login.userinfo;

import com.miniyus.friday.infrastructure.auth.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniyus.friday.common.validation.annotation.Enum;

/**
 * password authentication user info
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordUserInfo {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Enum(enumClass = UserRole.class, ignoreCase = true)
    private String role;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
