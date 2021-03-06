package com.donghun.todo.web.dto;

import com.donghun.todo.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotBlank(message = "name is null")
    private String name;

    @NotNull(message = "age is null")
    @Max(value = 150)
    private Integer age;

    @NotBlank(message = "password is null")
    @Length(min = 8, max = 22, message = "비밀번호는 8~22 사이로 작성해주서야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-_])(?=.*[0-9]).{8,22}$", message = "비밀번호 구성을 올바르게 하십시오")
    private String password;

    public User createUser(PasswordEncoder passwordEncoder, String imgUrl) {
        return User.builder().username(name).age(age).image(imgUrl)
                .password(passwordEncoder.encode(password)).build();
    }
}
