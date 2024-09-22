package com.doctorhoai.productqueryservice.service;

import com.doctorhoai.basecore.dto.ProductEvent;
import com.doctorhoai.productqueryservice.entity.Product;
import com.doctorhoai.productqueryservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepository productRepository;

    public List<Product> getProducts(){
        return productRepository.findAll();
    }
    @KafkaListener(topics = "product-event-topic", groupId = "doctorhoai")
    public void processProductEvents(ProductEvent productEvent ){
        Product product = new Product(productEvent.getId(),
                productEvent.getName(),
                productEvent.getDescription(),
                productEvent.getPrice()
                );
        if( productEvent.getEventType().equals("CreateProduct") ){
            productRepository.save(product);
        }
        else if( productEvent.getEventType().equals("UpdateProduct") ){
            Product existingProduct = productRepository.findById(product.getId()).get();
            existingProduct.setPrice(product.getPrice());
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
        }
    }
}
