package com.estate.back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// estate 데이터베이스의 qna_detail_passwd 테이블과 매핑되는 Entity 클래스

@Entity(name = "qnaDetailPasswd")
@Table(name = "qna_detail_passwd")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaDetailPasswdEntity 
{
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Integer num;
    private String userId;
    private String qnaDetailPasswd;
}
