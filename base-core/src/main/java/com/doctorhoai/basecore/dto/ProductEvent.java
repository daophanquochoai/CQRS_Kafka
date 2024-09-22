package com.doctorhoai.basecore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent implements Serializable {
    private final static Long serialVersion = 12312312312L;
    private String eventType;
    private Long id;
    private String name;
    private String description;
    private Double price;
}
