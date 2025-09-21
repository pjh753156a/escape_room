package com.estate.back.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 상세게시판 비밀번호 인증 Request Body Dto

@Getter
@Setter
@NoArgsConstructor
public class GetMatchQnaDetailPasswdRequestDto 
{
    @NotBlank
    private String qnaDetailPasswd;
}
