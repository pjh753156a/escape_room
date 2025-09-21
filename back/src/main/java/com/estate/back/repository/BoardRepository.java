package com.estate.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estate.back.entity.BoardEntity;

import jakarta.transaction.Transactional;

// estate 데이터베이스의 board 테이블 작업을 위한 리포지토리
public interface BoardRepository extends JpaRepository<BoardEntity,Integer>
{
    List<BoardEntity> findByOrderByReceptionNumberDesc();
    
    // Contains / Containing / IsContaining => LIKE '%word%'
    // StartingWith => LIKE 'word%'
    // EndingWith => LIKE '%word'
    List<BoardEntity> findByTitleContainsOrderByReceptionNumberDesc(String title);
    BoardEntity findByReceptionNumber(Integer receptionNumber);
    @Transactional
    void deleteByWriterId(String writerId);
}
/* 최종완료 */