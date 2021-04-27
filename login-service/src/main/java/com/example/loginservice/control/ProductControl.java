package com.example.loginservice.control;

import com.example.loginservice.common.Constants;
import com.example.loginservice.common.Response;
import com.example.loginservice.entity.Product;
import com.example.loginservice.repository.ProductRepository;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductControl {
    private static final Logger logger = LoggerFactory.getLogger(ProductControl.class);
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/fake")
    @Transactional
    public @ResponseBody
    Response fakeData() {
        List<Product> lstProduct = (List<Product>) productRepository.findAll();
        List<Product> lstSave = new ArrayList<>();
        NumberFormat numberFormat = new DecimalFormat("#0.00");
        for (int i = 0; i < lstProduct.size() + 10; i++) {
            Product product = new Product();
            product.setName("Product: " + i);
            product.setPrice(Double.valueOf(numberFormat.format((50 - 10) * new Random().nextDouble())));
            lstSave.add(product);
        }
        if (Objects.nonNull(lstSave)) {
            try {
                productRepository.saveAll(lstSave);
                return Response.success(Constants.RESPONSE_CODE.SUCCESS);
            } catch (Exception e) {
                logger.warn(e.getMessage());
                return Response.warning(Constants.RESPONSE_CODE.WARNING, e.getMessage());
            }
        } else {
            return Response.error(Constants.RESPONSE_CODE.NO_DATA_EVALUATION);
        }
    }

    @GetMapping("/getAll")
    public @ResponseBody
    Response getLst() {
        List<Product> lst = (List<Product>) productRepository.findAll();
        return Response.success().withData(lst);
    }
}
