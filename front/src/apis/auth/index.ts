import { DeleteUserRequestDto, EmailAuthCheckRequestDto, EmailAuthRequestDto, GetMatchQnaDetailPasswdRequestDto, GetMatchQnaWritePasswdRequestDto, IdCheckRequestDto, SignInRequestDto, SignUpRequestDto } from "src/apis/auth/dto/request/index";
import { SignInResponseDto } from "src/apis/auth/dto/response/index";
import ResponseDto from "src/apis/response.dto";

import axios from "axios";
import { bearerAuthorization, requestErrorHandler, requestHandler } from "src/apis/index";

import { DELETE_USER_INFO_URL, EMAIL_AUTH_CHECK_REQUEST_URL, EMAIL_AUTH_REQUEST_URL, GET_MATCH_QNA_DETAIL_PASSWD_REQUEST_URL, GET_MATCH_QNA_WRITE_PASSWD_REQUEST_URL, GET_SEARCH_QNA_DETAIL_PASSWD_REQUEST_URL, GET_SEARCH_QNA_WRITE_PASSWD_REQUEST_URL, ID_CHECK_REQUEST_URL, POST_UPLOAD_FILE_REUQEST_URL, SIGN_IN_REUQEST_URL, SIGN_UP_REQUEST_URL } from "src/constant";
import { access } from "node:fs/promises";

// function: 로그인 API 함수
export const SignInRequest = async (requestBody: SignInRequestDto) => 
{
    const result = await axios.post(SIGN_IN_REUQEST_URL,requestBody)
        .then(requestHandler<SignInResponseDto>)
        .catch(requestErrorHandler);
    return result;
};

// function: 아이디 중복 확인 API 함수
export const IdCheckRequest = async (requestBody: IdCheckRequestDto) => 
{
    const result = await axios.post(ID_CHECK_REQUEST_URL,requestBody)
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler);
    return result;
};

// function: 이메일 인증 API 함수
export const emailAuthRequest = async (requestBody :EmailAuthRequestDto) => 
{
    const result = await axios.post(EMAIL_AUTH_REQUEST_URL,requestBody)
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
};

// function: 이메일 인증 확인 API 함수
export const emailAuthCheckRequest = async(requestBody:EmailAuthCheckRequestDto) =>
{
    const result = await axios.post(EMAIL_AUTH_CHECK_REQUEST_URL, requestBody)
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
};

// function: 회원가입 API 함수
export const signUpRequest = async(requestBody : SignUpRequestDto) => 
{
    const result = await axios.post(SIGN_UP_REQUEST_URL, requestBody)
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}

// function: 회원탈퇴 API 함수
export const deleteUserInfoRequest = async(requestBody : DeleteUserRequestDto, accessToken: string) => 
{
    const result = await axios.post(DELETE_USER_INFO_URL, requestBody, bearerAuthorization(accessToken))
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}

// function: 글쓰기 비밀번호 일치여부 API 함수
export const getMatchQnaWritePasswdRequest = async(requestBody:GetMatchQnaWritePasswdRequestDto, accessToken:string) => 
{
    const result = await axios.post(GET_MATCH_QNA_WRITE_PASSWD_REQUEST_URL, requestBody, bearerAuthorization(accessToken))
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}

export const getMatchQnaDetailPasswdRequest = async(requestBody:GetMatchQnaDetailPasswdRequestDto, accessToken:string) => 
{
    const result = await axios.post(GET_MATCH_QNA_DETAIL_PASSWD_REQUEST_URL, requestBody, bearerAuthorization(accessToken))
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}

export const getSearchQnaWritePasswdRequest = async(requestBody:GetMatchQnaWritePasswdRequestDto, accessToken:string) => 
{
    const result = await axios.post(GET_SEARCH_QNA_WRITE_PASSWD_REQUEST_URL, requestBody, bearerAuthorization(accessToken))
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}

export const getSearchQnaDetailPasswdRequest = async(requestBody:GetMatchQnaDetailPasswdRequestDto, accessToken:string) => 
{
    const result = await axios.post(GET_SEARCH_QNA_DETAIL_PASSWD_REQUEST_URL, requestBody, bearerAuthorization(accessToken))
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}

export const postFileUploadRequest = async(formData:FormData) =>
{
    const result = await axios.post(POST_UPLOAD_FILE_REUQEST_URL, formData)
        .then(requestHandler<ResponseDto>)
        .catch(requestErrorHandler)
    return result;
}