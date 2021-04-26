package com.example.loginservice.service;

import com.example.loginservice.entity.Role;
import com.example.loginservice.entity.User;
import com.example.loginservice.enums.Permission;
import com.example.loginservice.repository.RoleRepository;
import com.example.loginservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public User registerUser(String email){
        Optional<User> optUser = userRepository.findByEmail(email);
        if (!optUser.isPresent()){
            User user = new User(email,passwordEncoder.encode("admin"));
            Role roleUser = roleRepository.findByName(Permission.ROLE_USER).get();
            Set<Role> roles = new HashSet<>();
            roles.add(roleUser);
            user.setRoles(roles);
            return userRepository.save(user);
        }else {
            return null;
        }
    }
}
