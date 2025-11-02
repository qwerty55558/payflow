package com.fds.payflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignFormDto {
    @NotBlank
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 8-15 자여야 하며 영문, 숫자, 특수문자를 최소 1 개씩 포함해야 합니다.")
    private String password;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호는 8-15 자여야 하며 영문, 숫자, 특수문자를 최소 1 개씩 포함해야 합니다.")
    private String passwordConfirm;

    @Override
    public String toString() {
        return "LoginFormDto{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                '}';
    }
}
