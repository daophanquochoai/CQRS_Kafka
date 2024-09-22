package com.doctorhoai.productqueryservice.controller;

import com.doctorhoai.basecore.dto.ProductEvent;
import com.doctorhoai.productqueryservice.entity.Product;
import com.doctorhoai.productqueryservice.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductQueryController {
    private final ProductQueryService productQueryService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Product>> fetchAllProducts(){
        return ResponseEntity.ok(productQueryService.getProducts());
    }
    public void processProductEvents(ProductEvent productEvent ){

    }
}
