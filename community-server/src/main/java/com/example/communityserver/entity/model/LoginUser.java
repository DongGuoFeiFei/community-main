package com.example.communityserver.entity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@NoArgsConstructor
// 用于储存用户的登录信息
// 解决后续redis读取数据时反序列化报错
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginUser implements UserDetails {

    private User user;

    // 权限集合
    private List<String> permissions = new ArrayList<>();

    private List<String> roles; // 新增角色信息
    // security需要的权限集合类型
    @JsonIgnore
    private List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    // 构造方法
    public LoginUser(User user, List<String> permissions, List<String> roles) {
        this.user = user;
        this.permissions = permissions != null ? permissions : Collections.emptyList();
        this.roles = roles != null ? roles : Collections.emptyList();
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public User getUser() {
        return user;
    }

    /**
     * 用来获取权限集合
     *
     * @return
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // 添加权限
        permissions.stream()
                .filter(perm -> perm != null && !perm.trim().isEmpty())
                .forEach(perm -> authorities.add(new SimpleGrantedAuthority(perm)));

        // 添加角色（可选，格式为 ROLE_角色名）
        roles.stream()
                .filter(role -> role != null && !role.trim().isEmpty())
                .forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role)));

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
