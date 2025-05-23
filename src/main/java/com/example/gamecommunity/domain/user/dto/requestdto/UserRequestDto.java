package com.example.gamecommunity.domain.user.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestDto {

    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    @NotBlank
    private final String nickname;

}
