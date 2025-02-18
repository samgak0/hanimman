package org.devkirby.hanimman.config;

import lombok.ToString;
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

    public CustomUserDetails(Integer id, String nickname, String codenum,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nickname = nickname;
        this.codenum = codenum;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public Integer getId() {
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
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
