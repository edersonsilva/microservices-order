package com.codevops.ordersaas.repository;

import com.codevops.ordersaas.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository  extends MongoRepository<Product, String> {
}
