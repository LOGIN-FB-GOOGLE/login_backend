package com.example.loginservice.service;

import com.example.loginservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
}
