package com.estate.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.estate.back.entity.QnaDetailPasswdEntity;
import com.estate.back.entity.QnaWritePasswdEntity;

// estate 데이터베이스의 qna_detail_passwd 테이블의 작업을 위한 리포지토리

@Repository
public interface QnaDetailPasswdRepository extends JpaRepository<QnaDetailPasswdEntity,Integer>
{
    boolean existsByUserIdAndQnaDetailPasswd(String userId, String qnaDetailPasswd);   
}
