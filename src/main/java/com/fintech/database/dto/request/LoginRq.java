package com.fintech.database.dto.request;

import com.fintech.database.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginRq {
    @Schema(example = "user34")
    @NotBlank
    private String username;

    @Schema(description = "Пароль должен состоять из букв, цифр и символов, " +
            "обязательно содержать заглавную латинскую букву, цифру и иметь длину не менее 8 символов.",
            example = "F1^23ghw&")
    @Password
    @NotNull
    private String password;

    @Schema(description = "Значение поля выбора <запомнить меня>", example = "true")
    private Boolean rememberMe;
}