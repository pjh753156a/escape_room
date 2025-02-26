package com.estate.back.service.implimentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.estate.back.dto.request.board.PostBoardRequestDto;
import com.estate.back.dto.request.board.PostCommentRequestDto;
import com.estate.back.dto.request.board.PutBoardRequestDto;
import com.estate.back.dto.response.ResponseDto;
import com.estate.back.dto.response.board.GetBoardListResponseDto;
import com.estate.back.dto.response.board.GetBoardResponseDto;
import com.estate.back.dto.response.board.GetSearchBoardListResponseDto;
import com.estate.back.entity.BoardEntity;
import com.estate.back.repository.BoardRepository;
import com.estate.back.repository.UserRepository;
import com.estate.back.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplementation implements BoardService
{
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    
    @Override
    public ResponseEntity<ResponseDto> postBoard(PostBoardRequestDto dto, String userId) 
    {
        try
        {
            boolean isExistUser = userRepository.existsByUserId(userId);
            if(!isExistUser) return ResponseDto.authenticationFailed();

            BoardEntity boardEntity = new BoardEntity(dto,userId);
            boardRepository.save(boardEntity);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
    
    @Override
    public ResponseEntity<? super GetBoardListResponseDto> getBoardList()
    {
        try
        {
            List<BoardEntity> boardEntities = boardRepository.findByOrderByReceptionNumberDesc();
            return GetBoardListResponseDto.success(boardEntities);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
    
    @Override
    public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoardList(String searchWord)
    {
        try
        {
            List<BoardEntity> boardEntities = boardRepository.findByTitleContainsOrderByReceptionNumberDesc(searchWord);
            return GetSearchBoardListResponseDto.success(boardEntities);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
    
    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoard(int receptionNumber)
    {
        try
        {
            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();

            //boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);

            return GetBoardResponseDto.success(boardEntity);
        }catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
    }
    
    @Override
    public ResponseEntity<ResponseDto> increaseViewCount(int receptionNumber)
    {
        try
        {
            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();

            boardEntity.increaseViewCount();
            boardRepository.save(boardEntity);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
    
    @Override
    public ResponseEntity<ResponseDto> postComment(PostCommentRequestDto dto, int receptionNumber) 
    {
        try
        {
            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();

            boolean status = boardEntity.getStatus();
            if(status) return ResponseDto.writtenComment();

            String comment = dto.getComment();
            boardEntity.setStatus(true);
            boardEntity.setComment(comment);

            boardRepository.save(boardEntity);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
    
    @Override
    public ResponseEntity<ResponseDto> deleteBoard(int receptionNumber, String userId)
    {
        try
        {
            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();

            String writerId = boardEntity.getWriterId();
            boolean isWriter = userId.equals(writerId);
            if(!isWriter) return ResponseDto.authorizationFailed();

            boardRepository.delete(boardEntity);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
    
    @Override
    public ResponseEntity<ResponseDto> putBoard(PutBoardRequestDto dto, int receptionNumber, String userId) 
    {
        try
        {
            BoardEntity boardEntity = boardRepository.findByReceptionNumber(receptionNumber);
            if(boardEntity == null) return ResponseDto.noExistBoard();

            String writerId = boardEntity.getWriterId();
            boolean isWriter = userId.equals(writerId);
            if(!isWriter) return ResponseDto.authenticationFailed();

            boolean status = boardEntity.getStatus();
            if(status) return ResponseDto.writtenComment();

            boardEntity.update(dto);
            boardRepository.save(boardEntity);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return ResponseDto.success();
    }
    
    // SELECT ~  entity OK / boolean

    // - 유효성 검사
    // (receptionNumber)
    // - 데이터베이스의 Board 테이블에서 receptionNumber에 해당하는 레코드 조회
    // SELECT * FROM board WHERE reception_number = :receptionNumber;


    // SELECT * FROM board WHERE title LIKE '%searchWord%' ORDER BY reception_number DESC;
    // findByTitleContainsOrderByReceptionNumberDesc();
    // GET Http://localhost:4000/api/vi/board/list/클라이언트

}

// 0. 클라이언트로부터 Authorization 헤더와 Request Body를 포함하여 요청
// 0.1 권한이 없는 사용자이면 'AF' 응답 처리 (403)
// 0.2 유효하지 않은 데이터이면 'VF' 응답 처리 (403)

// (title, contents), userId

// 1. 데이터베이스의 user 테이블에서 해당 유저가 존재하는지 확인
// 1.1 존재하지 않는 유저라면 'AF'응답 처리 (401)
// 1.2 데이터베이스 오류가 발생하면 'DBE' 응답 처리

// 2. Board 테이블에 데이터 삽입
// 2.1 데이터베이스 오류가 발생하면 'DBE'응답 처리

// 3. 'SU'응답 처리
/* 최종완료 */