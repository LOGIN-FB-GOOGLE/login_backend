package com.example.loginservice.service;

import com.example.loginservice.entity.Role;
import com.example.loginservice.entity.User;
import com.example.loginservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
//
//    public Optional<Role> getByName(String name){
//        return roleRepository.findByName(name);
//    }
//
//    public boolean existsName(String name){
//        return roleRepository.existsByName(name);
//    }
}
