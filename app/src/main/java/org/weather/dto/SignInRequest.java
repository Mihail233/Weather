package org.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 5, max = 100, message = "Username must be between 5 and 20 characters long")
    private String username;

    private String password;
}
