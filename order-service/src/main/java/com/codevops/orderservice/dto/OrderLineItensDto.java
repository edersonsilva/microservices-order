package com.codevops.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItensDto {
    private Long Id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}

