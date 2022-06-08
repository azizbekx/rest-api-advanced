package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.handler.DateHandler;
import com.epam.esm.mapper.OrderConvert;
import com.epam.esm.mapper.UserConvert;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.repository.UserDao;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private DateHandler dateHandler;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GiftCertificateDao giftDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public PaginationResult<OrderDto> getAll(EntityPage entityPage) {
        PaginationResult<Order> orderList = orderDao.list(entityPage);

        if (orderList.getRecords().isEmpty()) {
            throw new ResourceNotFoundException();
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

    @Override
    public OrderDto getById(long id) {
        return null;
    }

    @Override
    public OrderDto insert(OrderDto orderDto) {
        Order order = new Order();

        Optional<User> savedUser = userDao.getById(orderDto.getUser_id());
        Optional<GiftCertificate> savedGift = giftDao.getById(orderDto.getGift_certificate_id());

        if (savedGift.isEmpty()) {
            throw new ResourceNotFoundException(orderDto.getGift_certificate_id());
        }
        if (savedUser.isEmpty()) {
            throw new ResourceNotFoundException(orderDto.getUser_id());
        }

        order.setUser(savedUser.get());
        order.setGiftCertificate(savedGift.get());
        order.setPrice(savedGift.get().getPrice());
        return OrderConvert.toDto(orderDao.insert(order));
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }

    @Override
    public UserDto saveByUser(long userId, Set<Long> giftIds) {
        if (giftIds.isEmpty()) {
            return null;
        }
        Optional<User> user = userDao.getById(userId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(userId);
        }
        for (Long giftId : giftIds) {
            insert(new OrderDto(userId, giftId));
        }
        return UserConvert.toDto(user.get());
    }

    @Override
    public List<OrderDto> getOrderByUser(long userId) {
        return orderDao.getOrdersByUser(userId)
                .stream()
                .map(OrderConvert::toDto)
                .collect(Collectors.toList());
    }
}
