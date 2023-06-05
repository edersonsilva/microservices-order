package com.codevops.orderservice.service;

import com.codevops.orderservice.dto.OrderLineItensDto;
import com.codevops.orderservice.dto.OrderRequest;
import com.codevops.orderservice.model.Order;
import com.codevops.orderservice.model.OrderLineItens;
import com.codevops.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItens> orderLineItens = new ArrayList<>();
        for (OrderLineItensDto orderLineItensDto : orderRequest.getOrderLineItensDtoList()) {
            OrderLineItens lineItens = mapToDto(orderLineItensDto);
            orderLineItens.add(lineItens);
        }

        order.setOrderLineItensList(orderLineItens);

        orderRepository.save(order);
    }

    private OrderLineItens mapToDto(OrderLineItensDto orderLineItensDto) {
        OrderLineItens orderLineItens = new OrderLineItens();
        orderLineItens.setPrice(orderLineItensDto.getPrice());
        orderLineItens.setSkuCode(orderLineItensDto.getSkuCode());
        orderLineItens.setQuantity(orderLineItensDto.getQuantity());
        return orderLineItens;
    }
}
