package com.estate.back.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 글쓰기 비밀번호 인증 Request Body Dto

@Getter
@Setter
@NoArgsConstructor
public class GetMatchQnaWritePasswdRequestDto 
{
    @NotBlank
    private String qnaWritePasswd;
}
