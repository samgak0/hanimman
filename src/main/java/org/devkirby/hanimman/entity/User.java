package org.devkirby.hanimman.entity;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    /**
     * 사용자의 고유 식별자 (기본 키)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 사용자의 실명
     */
    @Column(length = 20, nullable = false)
    private String name;

    /**
     * 사용자의 생년월일
     */
    @Column(nullable = false)
    private LocalDate birth;

    /**
     * 사용자의 성별
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    /**
     * 사용자의 전화번호
     */
    @Column(length = 20, nullable = false)
    private String phonenum;

    /**
     * 사용자의 닉네임
     */
    @Column(length = 20, nullable = false)
    private String nickname;

    /**
     * 회원가입 시 자동 생성되는 고유 코드 번호 (중복 없음)
     */
    @Column(length = 6, nullable = false)
    private String codenum;

    /**
     * 사용자 계정 차단 시간
     */
    @Column
    private Instant blockedAt;

    /**
     * 사용자 정보 최종 수정 시간
     */
    @Column
    private Instant modifiedAt;

    /**
     * 사용자 계정 생성 시간 (기본값: 현재 시간)
     */
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    /**
     * 사용자 계정 삭제 시간
     */
    @Column
    private Instant deletedAt;

    /**
     * 사용자의 당도 점수 (당근의 온도와 유사한 사용자 점수, 기본값: 15)
     */
    @Column
    @Builder.Default
    private Integer brix = 15;

    /**
     * 엔티티가 데이터베이스에 저장되기 전에 실행되는 메서드
     * 생성 시간과 당도 점수를 기본값으로 설정
     */
    @PrePersist
    public void prePersist() { 
        if (this.createdAt == null) {
            this.createdAt = Instant.now();  
        }
        if (this.brix == null){
            this.brix = 15;
        }
    }

}
