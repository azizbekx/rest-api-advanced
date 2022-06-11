package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.pagination.PaginationResult;

public interface OrderDao extends BasicDao<Order> {
    PaginationResult<Order> getOrdersByUser(long userId, EntityPage entityPage);
}
