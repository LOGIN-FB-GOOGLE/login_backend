package com.example.loginservice.service;

import com.example.loginservice.control.ProductControl;
import com.example.loginservice.entity.Role;
import com.example.loginservice.entity.User;
import com.example.loginservice.enums.Permission;
import com.example.loginservice.repository.RoleRepository;
import com.example.loginservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    public User registerUser(String email){
        if (Objects.isNull(getUser(email))){
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

    private User getUser(String email){
        try {
            Optional<User> optUser = userRepository.findUserByEmail(email);
            if (!optUser.isPresent()){
                return optUser.get();
            }else {
                return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
