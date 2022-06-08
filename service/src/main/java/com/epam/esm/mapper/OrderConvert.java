package com.epam.esm.mapper;


import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;

public class OrderConvert {
    public static OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setPrice(order.getPrice());
        orderDto.setCreate_date(order.getCreateDate().toString());
        if (order.getGiftCertificate() != null) {
            orderDto.setGift_certificate_id(order.getGiftCertificate().getId());
        }
        if (order.getUser() != null) {
            orderDto.setUser_id(order.getUser().getId());
        }
        return orderDto;
    }
}
