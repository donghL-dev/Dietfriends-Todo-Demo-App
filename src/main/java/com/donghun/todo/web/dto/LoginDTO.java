package com.donghun.todo.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class LoginDTO {

    @NotBlank(message = "name is null")
    private String name;

    @NotBlank(message = "password is null")
    @Length(min = 8, max = 22, message = "비밀번호는 8~22 사이로 작성해주서야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-_])(?=.*[0-9]).{8,22}$", message = "비밀번호 구성을 올바르게 하십시오")
    private String password;
}
