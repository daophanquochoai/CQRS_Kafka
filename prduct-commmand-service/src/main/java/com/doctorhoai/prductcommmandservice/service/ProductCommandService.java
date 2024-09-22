package com.doctorhoai.prductcommmandservice.service;

import com.doctorhoai.basecore.dto.ProductEvent;
import com.doctorhoai.prductcommmandservice.entity.Product;
import com.doctorhoai.prductcommmandservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public Product createProduct( Product product){
        Product productSave = productRepository.save(product);
        ProductEvent event = new ProductEvent("CreateProduct", productSave.getId(),
                productSave.getName(),
                productSave.getDescription(),
                productSave.getPrice());
        kafkaTemplate.send("product-event-topic", event);
        return productSave;
    }
    public Product updateProduct( Long id, Product product){
        Product existProduct = productRepository.findById(id).get();
        existProduct.setName(product.getName());
        existProduct.setDescription(product.getDescription());
        existProduct.setPrice(product.getPrice());
        Product productUpdated = productRepository.save(existProduct);
        ProductEvent event = new ProductEvent("UpdateProduct", productUpdated.getId(),
                productUpdated.getName(),
                productUpdated.getDescription(),
                productUpdated.getPrice());
        kafkaTemplate.send("product-event-topic", event);
        return productUpdated;
    }
}
