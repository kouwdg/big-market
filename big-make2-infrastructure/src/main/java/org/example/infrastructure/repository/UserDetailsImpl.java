package org.example.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.infrastructure.persistent.po.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: TODO
 * @date 2025/3/24 13:23
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private UserAccount userAccount;

    //返回权限信息
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userAccount==null||userAccount.getRoleList()==null){
            return Collections.emptyList();
        }
        return userAccount.getRoleList().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    //返回密码
    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    //返回用户账号
    @Override
    public String getUsername() {
        return userAccount.getUserName();
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

    //检查账户是否使用
    @Override
    public boolean isEnabled() {
        return true;
    }
}
