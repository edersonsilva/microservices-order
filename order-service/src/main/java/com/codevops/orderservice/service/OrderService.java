package com.codevops.orderservice.service;

import com.codevops.orderservice.dto.InventoryResponse;
import com.codevops.orderservice.dto.OrderLineItensDto;
import com.codevops.orderservice.dto.OrderRequest;
import com.codevops.orderservice.model.Order;
import com.codevops.orderservice.model.OrderLineItens;
import com.codevops.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItens> orderLineItens = new ArrayList<>();
        for (OrderLineItensDto orderLineItensDto : orderRequest.getOrderLineItensDtoList()) {
            OrderLineItens lineItens = mapToDto(orderLineItensDto);
            orderLineItens.add(lineItens);
        }

        order.setOrderLineItensList(orderLineItens);

        List<String> skuCodes = order.getOrderLineItensList().stream()
                .map(OrderLineItens::getSkuCode)
                .toList();

        // Chama o serviço de Inventário e place order se o produto está em estoque
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                .uri("http://localhost:808/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock!");
        }

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