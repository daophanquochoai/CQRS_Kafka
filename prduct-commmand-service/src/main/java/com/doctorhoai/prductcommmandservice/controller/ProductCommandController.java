package com.doctorhoai.prductcommmandservice.controller;

import com.doctorhoai.prductcommmandservice.entity.Product;
import com.doctorhoai.prductcommmandservice.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductCommandController {
    private final ProductCommandService productCommandService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product ){
        Product productNew =  productCommandService.createProduct(product);
        return ResponseEntity.ok(productNew);
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody Product product,
            @PathVariable Long id
    ){
        Product productUpdated = productCommandService.updateProduct(id, product);
        return ResponseEntity.ok(productUpdated);
    }

}
