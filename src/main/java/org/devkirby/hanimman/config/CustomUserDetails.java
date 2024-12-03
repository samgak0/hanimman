package org.devkirby.hanimman.config;

import lombok.ToString;
import org.devkirby.hanimman.entity.Address;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;

@ToString
public class CustomUserDetails implements UserDetails {

    private String nickname;
    private String codenum;
    private Instant blockedAt;
    private Instant createdAt;
    private Instant deletedAt;
    private Collection<? extends GrantedAuthority> authorities;
    private Integer id;
    private Address primaryAddressId;



    // 생성자
    public CustomUserDetails(Integer id, String nickname, String codenum, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nickname = nickname;
        this.codenum = codenum;
        this.authorities = authorities;
    }

    // UserDetails 인터페이스 메서드 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        // 비밀번호가 필요 없다면 null 또는 빈 문자열을 반환
        return null;
    }

    public Integer getId(){
        return id;
    }

    @Override
    public String getUsername() {
        return "";
    }


    public String getNickname() {
        return nickname;
    }

    public String getCodenum() {
        return codenum;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 예시에서는 만료되지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 자격증명 만료되지 않음
    }

    public Instant getBlockedAt() {
        return blockedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}
