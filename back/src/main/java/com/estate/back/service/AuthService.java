package com.estate.back.service;

import org.springframework.http.ResponseEntity;

import com.estate.back.dto.request.auth.DeleteUserRequestDto;
import com.estate.back.dto.request.auth.EmailAuthCheckRequestDto;
import com.estate.back.dto.request.auth.EmailAuthRequestDto;
import com.estate.back.dto.request.auth.GetMatchQnaDetailPasswdRequestDto;
import com.estate.back.dto.request.auth.GetMatchQnaWritePasswdRequestDto;
import com.estate.back.dto.request.auth.SignInRequestDto;
import com.estate.back.dto.request.auth.SignUpRequestDto;
import com.estate.back.dto.request.auth.idCheckRequestDto;
import com.estate.back.dto.response.ResponseDto;
import com.estate.back.dto.response.auth.SignInResponseDto;

public interface AuthService
{
    ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto);
    ResponseEntity<ResponseDto> idCheck(idCheckRequestDto dto);
    ResponseEntity<ResponseDto> emailAuth(EmailAuthRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn (SignInRequestDto dto);
    ResponseEntity<ResponseDto> emailAuthCheck(EmailAuthCheckRequestDto dto);
    ResponseEntity<ResponseDto> deleteUser(DeleteUserRequestDto requestBody,String userId);
    //수정
    ResponseEntity<ResponseDto> matchQnaWritePasswd(GetMatchQnaWritePasswdRequestDto requestBody, String userId);
    ResponseEntity<ResponseDto> matchQnaDetailPasswd(GetMatchQnaDetailPasswdRequestDto requestDto, String userId);
    ResponseEntity<ResponseDto> searchQnaWritePasswd(GetMatchQnaWritePasswdRequestDto requestBody, String userId);
    ResponseEntity<ResponseDto> searchQnaDetailPasswd(GetMatchQnaDetailPasswdRequestDto requestBody, String userId);
}
//수정
/* 최종완료 */
