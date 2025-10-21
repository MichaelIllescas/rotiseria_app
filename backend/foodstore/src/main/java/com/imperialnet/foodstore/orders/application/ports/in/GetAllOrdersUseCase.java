package com.imperialnet.foodstore.orders.application.ports.in;

import com.imperialnet.foodstore.orders.domain.model.Order;

import java.util.List;

public interface GetAllOrdersUseCase {

    public List<Order> getAllOrders();
}
