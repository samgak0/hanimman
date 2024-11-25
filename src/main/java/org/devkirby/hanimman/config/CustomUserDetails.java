package org.devkirby.hanimman.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String username;
    private Instant blockedAt;
    private Instant deletedAt;
    private Collection<? extends GrantedAuthority> authorities;

    // 생성자
    public CustomUserDetails(String username,  Instant blockedAt, Instant deletedAt, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.blockedAt = blockedAt;
        this.deletedAt = deletedAt;
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

    @Override
    public String getUsername() {
        return username;
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
}
