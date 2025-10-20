package com.fds.payflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.Message;

@Getter
@Setter
public class LoginFormDto {
    @NotBlank
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 8-15 자여야 하며 영문, 숫자, 특수문자를 최소 1 개씩 포함해야 합니다.")
    private String password;

    @Override
    public String toString() {
        return "LoginFormDto{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
