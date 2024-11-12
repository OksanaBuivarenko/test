package com.fintech.database.dto.request;

import com.fintech.database.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRq {
    @Schema(example = "user34")
    @NotBlank
    private String username;

    @NotBlank
    @Email
    @Schema(example = "user34@mail.ru")
    private String email;

    @Schema(description = "Пароль должен состоять из букв, цифр и символов, " +
            "обязательно содержать заглавную латинскую букву, цифру и иметь длину не менее 8 символов.",
            example = "F1^23ghw&")
    @Password
    @NotBlank
    private String password;
}