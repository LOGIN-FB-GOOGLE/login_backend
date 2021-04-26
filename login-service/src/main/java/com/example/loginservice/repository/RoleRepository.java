package com.example.loginservice.repository;

import com.example.loginservice.entity.Role;
import com.example.loginservice.enums.Permission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role,Integer> {
    Optional<Role> findByName(Permission name);

    boolean existsByName(Permission name);
}
