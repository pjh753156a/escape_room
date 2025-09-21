package com.estate.back.dto.response;

// 200 : SU/success.
// 400 필수 데이터 미입력: VF / Varidation Failed.
// 400 중복된 아이디: DI/Duplicatied Id.
// 400 중복된 이메일: DE/Duplicatied Email.
// 401 로그인 정보 불일치: SF/Sign in Failed.
// 401 인증 실패: AF / Authentication Failed.
// 401 패스워드 불일치: PN / Password does not match.
// 500 토큰 생성 실패:TF/Token creation Failed
// 500 이메일 전송 실패:MF/Mail send Failed.
// 500 데이터베이스오류: DBE/Database Error.

// Response의 공통된 code 값
public interface ResponseCode
{
    String SUCCESS = "SU";
    String NOT_FOUND="NF";
    String DUPLICATED_ID="DI";
    String SIGN_IN_FAILED="SF";
    String DATABASE_ERROR="DBE";
    String DUPLICATED_EMAIL="DE";
    String NO_EXIST_BOARD = "NB";
    String MAIL_SEND_FAILED="MF";
    String WRITTEN_COMMENT = "WC";
    String VALIDATION_FAILED ="VF";
    String AUTHENTICATION_FAILED="AF";
    String TOKEN_CREATION_FAILED="TF";
    String AUTHORIZATION_FAILED = "AF";
    String PASSWORD_DOES_NOT_MATCH = "PN";
}
/* 최종완료 */