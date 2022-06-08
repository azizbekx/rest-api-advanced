package com.epam.esm.repository;

import com.epam.esm.entity.Order;

import java.util.List;

public interface OrderDao extends BasicDao<Order> {
    List<Order> getOrdersByUser(long userId);
}
