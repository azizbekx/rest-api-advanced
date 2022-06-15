package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.OrderConvert;
import com.epam.esm.mapper.UserConvert;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.repository.UserDao;
import com.epam.esm.service.OrderService;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GiftCertificateDao giftDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public PaginationResult<OrderDto> getAll(EntityPage entityPage) {
        PaginationResult<Order> orderList = orderDao.list(entityPage);
        //Converting Order PaginateResult to PaginationResult OrderDto
        return converter(orderList, entityPage);
    }

    @Override
    public OrderDto getById(long id) {
        Optional<Order> optionalOrder = orderDao.getById(id);
        if (optionalOrder.isEmpty()) {
            throw new ResourceNotFoundException("Requested resource not found ( id = " + id + " )");
        }
        return OrderConvert.toDto(optionalOrder.get());
    }

    @Override
    public OrderDto insert(OrderDto orderDto) {
        if (orderDto.getGift_certificates() == null) {
            throw new IllegalArgumentException("Gift Certificate not entered");
        }
        Order order = new Order();
        Optional<User> savedUser = userDao.getById(orderDto.getUser_id());
        if (savedUser.isEmpty()) {
            throw new ResourceNotFoundException("Requested resource not found ( id = " + orderDto.getUser_id() + " )");
        }
        List<GiftCertificate> reqGiftList = new ArrayList<>();
        BigDecimal price = BigDecimal.valueOf(0);
        for (GiftCertificateDto giftDto : orderDto.getGift_certificates()) {
            Optional<GiftCertificate> savedGift = giftDao.getById(giftDto.getId());
            if (savedGift.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Requested resources not found (id = " + giftDto.getId() + " )");
            }
            reqGiftList.add(savedGift.get());
            price = price.add(savedGift.get().getPrice());
        }

        order.setGiftCertificates(new HashSet<>(reqGiftList));
        order.setPrice(price);
        order.setUser(savedUser.get());
        return OrderConvert.toDto(orderDao.insert(order));
    }

    @Override
    public boolean deleteById(long id) {
        throw new NotYetImplementedException();
    }

    // users/1/orders
    // list of gift
    @Override
    public UserDto saveByUser(long userId, List<GiftCertificateDto> giftDtos) {
        insert(new OrderDto(
                userId, giftDtos));
        Optional<User> optionalUser = userDao.getById(userId);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("Requested resource not found ( id = " + userId + " )");
        }
        return UserConvert.toDto(optionalUser.get());
    }

    @Override
    public PaginationResult<OrderDto> getOrderByUser(long userId, EntityPage entityPage) {
        PaginationResult<Order> orderList = orderDao.getOrdersByUser(userId, entityPage);
        //Converting Order PaginateResult to PaginationResult OrderDto
        return converter(orderList, entityPage);
    }

    private PaginationResult<OrderDto> converter(PaginationResult<Order> orderList, EntityPage entityPage) {
        if (entityPage.getPage() == 1 && orderList.getRecords().isEmpty()) {
            throw new ResourceNotFoundException("Resource not found");
        }
        List<OrderDto> orderDtos = orderList.getRecords()
                .stream()
                .map(OrderConvert::toDto)
                .collect(Collectors.toList());
        return new PaginationResult<>(
                new Page(
                        orderList.getPage().getCurrentPageNumber(),
                        orderList.getPage().getLastPageNumber(),
                        orderList.getPage().getPageSize(),
                        orderList.getPage().getTotalRecords()),
                orderDtos
        );
    }
}
