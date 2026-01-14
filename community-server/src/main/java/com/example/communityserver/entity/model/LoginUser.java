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

    // TODO: 2025/8/11 后续添加用户单独关联api（禁止【例如用户发布文章等】或允许）和菜单
    // TODO: 2025/8/14 后续用户的身份角色等权限单独缓存redis，方便后面不影响登录使用 改变用户权限
    // 权限集合
    private List<String> apis;  // 角色关联api
    private List<String> roles; // 角色身份
    private List<String> menus; // 角色关联菜单
    // security需要的权限集合类型    标记@JsonIgnore避免序列化到Redis
    @JsonIgnore
    private List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    // 构造方法
    public LoginUser(User user, List<String> apis, List<String> roles, List<String> menus) {
        this.user = user;
        this.apis = apis != null ? apis : Collections.emptyList();
        this.roles = roles != null ? roles : Collections.emptyList();
        this.menus = menus != null ? menus : Collections.emptyList();
    }

    /**
     * 用来获取权限集合
     *
     * @return
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (!grantedAuthorities.isEmpty()) {
            return grantedAuthorities;
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // 添加权限
        apis
                .stream()
                .filter(api -> api != null && !api.trim().isEmpty())
                .forEach(api -> authorities.add(new SimpleGrantedAuthority(api)));

        roles.stream().filter(role -> role != null && !role.trim().isEmpty()).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));

        menus.stream().filter(menu -> menu != null && !menu.trim().isEmpty()).forEach(menu -> authorities.add(new SimpleGrantedAuthority(menu)));

        grantedAuthorities.addAll(authorities);
        return grantedAuthorities;
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
