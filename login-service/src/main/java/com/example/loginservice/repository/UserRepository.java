package com.example.loginservice.repository;

import com.example.loginservice.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
public interface UserRepository extends CrudRepository<User,Integer> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
