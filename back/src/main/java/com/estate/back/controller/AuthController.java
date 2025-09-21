package com.estate.back.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
import com.estate.back.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Auth 모듈 컨트롤러 : /api/vi/auth
@RestController
@RequestMapping("/api/vi/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AuthService authService;
    
    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(
        @RequestBody @Valid SignInRequestDto requestBody
    )
    {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }
    
    @PostMapping("/id-check")
    public ResponseEntity<ResponseDto> idCheck(
        @RequestBody @Valid idCheckRequestDto requestBody
    )
    {
        ResponseEntity<ResponseDto> response = authService.idCheck(requestBody);
        return response;
    }
    
    @PostMapping("/email-auth")
    public ResponseEntity<ResponseDto> emailAuth(
        @RequestBody @Valid EmailAuthRequestDto requestBody
    )
    {
        ResponseEntity<ResponseDto> response = authService.emailAuth(requestBody);
        return response;
    }
    
    @PostMapping("/email-auth-check")
    public ResponseEntity<ResponseDto> emailAuthCheck(
        @RequestBody @Valid EmailAuthCheckRequestDto requestBody
    )
    {
        ResponseEntity<ResponseDto> response = authService.emailAuthCheck(requestBody);
        return response;
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signUp(
        @RequestBody @Valid SignUpRequestDto requestBody
    )
    {
        ResponseEntity<ResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @PostMapping("/delete-user")
    public ResponseEntity<ResponseDto> deleteUser(
        @RequestBody @Valid DeleteUserRequestDto requestBody,
        @AuthenticationPrincipal String userId
    )
    {
        ResponseEntity<ResponseDto> response = authService.deleteUser(requestBody, userId);
        return response;
    }

    @PostMapping("/get_match_qna_write_passwd")
    public ResponseEntity<ResponseDto> matchQnaWritePasswd(
        @RequestBody @Valid GetMatchQnaWritePasswdRequestDto requestBody,
        @AuthenticationPrincipal String userId
    )
    {
        ResponseEntity<ResponseDto> response = authService.matchQnaWritePasswd(requestBody, userId);
        return response;
    }

    @PostMapping("/get_match_qna_detail_passwd")
    public ResponseEntity<ResponseDto> matchQnaDetailPasswd(
        @RequestBody @Valid GetMatchQnaDetailPasswdRequestDto requestBody,
        @AuthenticationPrincipal String userId
    )
    {
        ResponseEntity<ResponseDto> response = authService.matchQnaDetailPasswd(requestBody, userId);
        return response;
    }

    @PostMapping("/get_search_qna_write_passwd")
    public ResponseEntity<ResponseDto> searchQnaWritePasswd(
        @RequestBody @Valid GetMatchQnaWritePasswdRequestDto requestBody,
        @AuthenticationPrincipal String userId
    )
    {
        ResponseEntity<ResponseDto> response = authService.searchQnaWritePasswd(requestBody, userId);
        return response;
    }

    @PostMapping("/get_search_qna_detail_passwd")
    public ResponseEntity<ResponseDto> searchQnaDetailPasswd(
        @RequestBody @Valid GetMatchQnaDetailPasswdRequestDto requestBody,
        @AuthenticationPrincipal String userId
    )
    {
        ResponseEntity<ResponseDto> response = authService.searchQnaDetailPasswd(requestBody, userId);
        return response;
    }

    private final String uploadPath = "C:/uploads/"; // 윈도우 경로에 맞춰서

    @PostMapping("/upload")
    public ResponseEntity<ResponseDto> upload(@RequestParam("file") MultipartFile file) throws IOException 
    {
        try
        {
             File uploadDir = new File(uploadPath);
             
            // 디렉토리가 존재하지 않으면 생성
            if (!uploadDir.exists()) 
            {
                uploadDir.mkdirs();
            }

            File targetFile = new File(uploadPath + file.getOriginalFilename());
            file.transferTo(targetFile);
        }
        catch (Exception e)
        {
            e.printStackTrace(); // 로그라도 남기는 것이 좋습니다
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam String file) throws IOException 
    {
        Path path = Paths.get(uploadPath + file);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName().toString() + "\"")
            .header("X-File-Path", "final_password=black_label_project1")
            .body(resource);
    }
}
/* 최종완료 */