package com.example.loginservice.auth;

import com.example.loginservice.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
public class UserPrincipalFactory {
    public static UserPrincipal build(User user){
        List<GrantedAuthority> authorities =
                user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserPrincipal(user.getEmail(),user.getPassword(),authorities);
    }
}
