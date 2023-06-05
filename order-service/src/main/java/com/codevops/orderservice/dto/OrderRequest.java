package com.codevops.orderservice.dto;

import com.codevops.orderservice.model.OrderLineItens;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<OrderLineItensDto> orderLineItensDtoList;
}
