package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserConvert {
    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        if (user.getOrders() != null) {
            List<OrderDto> orderDtos = user.getOrders()
                    .stream()
                    .map(OrderConvert::toDto)
                    .collect(Collectors.toList());
            userDto.setOrders(orderDtos);
        }
        return userDto;
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        if (userDto.getOrders() != null) {
            Set<Order> orders = userDto.getOrders()
                    .stream()
                    .map(OrderConvert::toEntity)
                    .collect(Collectors.toSet());
            user.setOrders(orders);
        }
        return user;
    }

}
