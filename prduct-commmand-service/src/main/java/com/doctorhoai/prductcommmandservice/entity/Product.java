package com.doctorhoai.prductcommmandservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "PRODUCT_COMMAND" )
public class Product implements Serializable {
    private final static Long serialVersion = 12312312312L;
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Double price;
}
