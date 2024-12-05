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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // PK

    @Column(length = 20, nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private LocalDate birth; // 생일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender; // 성별

    @Column(length = 20, nullable = false)
    private String phonenum; // 핸드폰 번호

    @Column(length = 20, nullable = false)
    private String nickname; // 닉네임

    @Column(length = 6, nullable = false)
    private String codenum; // 코드번호(회원가입 때 자동 생성. 중복X)

    @Column
    private Instant blockedAt; // 블록처리 여부

    @Column
    private Instant modifiedAt; // 회원 수정 일자

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now(); // 회원 가입 일자

    @Column
    private Instant deletedAt; // 회원 탈퇴 일자

    @Column
    @Builder.Default
    private Integer brix = 15; // 당도 (당근의 온도 같은 것 유저 점수)

    @PrePersist
    public void prePersist() { // 회원가입 될 때 DB에 저장기 전에 이 로직이 한 번 실행되고 저장됩니다.
        if (this.createdAt == null) {
            this.createdAt = Instant.now();  // 엔티티가 저장될 때 현재 시간으로 설정
        }
        if (this.brix == null){
            this.brix = 15;
        }
    }

}
