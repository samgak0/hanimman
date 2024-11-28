package org.devkirby.hanimman.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.devkirby.hanimman.entity.Gender;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Integer id; // PK
    private String name; // 이름
    private LocalDate birth; // 생년월일
    private Gender gender; // 성별
    private String phonenum; //핸드폰번호
    private String nickname; // 닉네임
    private String codenum; // 코드번호
    private Boolean privilege; // 관리자 여부
    private Instant blockedAt; // 블록된 여부
    private Integer primaryAddressId; // 동네1
    private Integer secondlyAddressId; // 동네2
    private String deviceUniqueNum; // 기기 번호
    private Instant validatedAt; // 동네 인증 여부(인증된 일자)
    private Instant modifiedAt; // 수정 여부(수정된 일자)
    private Instant createdAt; // 가입 일자
    private Instant deletedAt; // 삭제 일자
    private Integer brix; // 당도

}
