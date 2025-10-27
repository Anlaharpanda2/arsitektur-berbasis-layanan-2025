package com.anla.Order.repository.mongo;

import com.anla.Order.model.OrderReadModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderReadRepository extends MongoRepository<OrderReadModel, String> {
    Optional<OrderReadModel> findByOrderId(String orderId);
}