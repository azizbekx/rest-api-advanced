package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Set;

public interface OrderService extends BasicService<OrderDto> {
    UserDto saveByUser(long id, Set<Long> giftIds);

    List<OrderDto> getOrderByUser(long userId);
}
