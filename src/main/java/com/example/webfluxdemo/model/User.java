package com.example.webfluxdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {

    @Id
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 100, message = "密码长度必须在8-100个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
             message = "密码必须包含大小写字母、数字和特殊字符")
    private String password;

    @NotNull(message = "用户状态不能为空")
    private Boolean isActive = true;

    @Size(max = 500, message = "个人简介不能超过500个字符")
    private String bio;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 设置密码（自动加密）
     */
    public void setPasswordEncoded(String rawPassword, com.example.webfluxdemo.security.PasswordUtils passwordUtils) {
        if (rawPassword != null && !passwordUtils.isPasswordStrong(rawPassword)) {
            throw new IllegalArgumentException("密码不符合强度要求");
        }
        this.password = passwordUtils.encodePassword(rawPassword);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 验证密码
     */
    public boolean verifyPassword(String rawPassword, com.example.webfluxdemo.security.PasswordUtils passwordUtils) {
        return passwordUtils.matches(rawPassword, this.password);
    }

    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void setBio(String bio) {
        this.bio = bio;
        this.updatedAt = LocalDateTime.now();
    }
}