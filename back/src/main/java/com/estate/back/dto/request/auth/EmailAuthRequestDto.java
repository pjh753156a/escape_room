package com.estate.back.dto.request.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//  이메일 인증 Request Body Dto

@Getter
@Setter
@NoArgsConstructor
public class EmailAuthRequestDto
{
    @NotNull
    //@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String userEmail;
}
/* 최종완료 */