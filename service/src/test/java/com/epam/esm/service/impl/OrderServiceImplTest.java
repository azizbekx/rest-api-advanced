package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.mapper.GiftConverter;
import com.epam.esm.mapper.OrderConvert;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.repository.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private final Tag TAG_1 = new Tag(1, "tag_1");
    private final Tag TAG_2 = new Tag(2, "tag_2");

    private final User USER_1 = new User(1, "Tomas");
    private final OrderDto ORDER_22 = new OrderDto(
            1, List.of(new GiftCertificateDto(2)));
    private final GiftCertificate GIFT_CERTIFICATE_1 =
            new GiftCertificate(1, "Gift_1", "For holiday", new BigDecimal("10.9"), 3,
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    new HashSet<>(Arrays.asList(TAG_1, TAG_2)));
    private final Order ORDER_1 = new Order(1, new BigDecimal("10.9"),
            LocalDateTime.parse("2022-03-29T06:12:15.156"),
            USER_1, new HashSet<>(List.of(GIFT_CERTIFICATE_1))
    );
    private final GiftCertificate GIFT_CERTIFICATE_2 =
            new GiftCertificate(2, "Gift_2", "For good work", new BigDecimal("20.9"), 3,
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    new HashSet<>(List.of(TAG_1)));
    private final Order ORDER_2 = new Order(2, new BigDecimal("20.9"),
            LocalDateTime.parse("2022-03-29T06:12:15.156"),
            USER_1, new HashSet<>(List.of(GIFT_CERTIFICATE_2))
    );
    private final Order ORDER_21 = new Order(new BigDecimal("20.9"),
            USER_1, new HashSet<>(List.of(GIFT_CERTIFICATE_2))
    );
    private final OrderDto ORDER_23 = new OrderDto(2, new BigDecimal("20.9"),
            "2022-03-29T06:12:15.156",
            1, List.of(GiftConverter.toDto(GIFT_CERTIFICATE_2)));
    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderDao orderDao = Mockito.mock(OrderDao.class);
    @Mock
    private GiftCertificateDao giftDao = Mockito.mock(GiftCertificateDao.class);
    @Mock
    private UserDao userDao = Mockito.mock(UserDao.class);

    @Test
    void getAll() {
        EntityPage entityPage = new EntityPage(0, 2);
        when(orderDao.list(entityPage)).thenReturn(createPaginateResult());

        PaginationResult<Order> actual = converter(orderService.getAll(entityPage));
        PaginationResult<Order> expected = createPaginateResult();
        assertEquals(expected, actual);
    }

    @Test
    void getById() {
        when(orderDao.getById(ORDER_1.getId())).thenReturn(Optional.of(ORDER_1));
        when(userDao.getById(USER_1.getId())).thenReturn(Optional.of(USER_1));

        OrderDto actualDto = orderService.getById(ORDER_1.getId());
        Order actual = OrderConvert.toEntity(actualDto);

        actual.setUser(userDao.getById(actualDto.getUser_id()).get());
        assertEquals(ORDER_1, actual);
    }

    @Test
    void insert() {
        when(userDao.getById(USER_1.getId())).thenReturn(Optional.of(USER_1));
        when(giftDao.getById(2)).thenReturn(Optional.of(GIFT_CERTIFICATE_2));
        when(orderDao.insert(ORDER_21)).thenReturn(ORDER_2);
        OrderDto actual = orderService.insert(ORDER_22);

        assertEquals(ORDER_23, actual);
    }

    @Test
    void saveByUser() {
        when(userDao.getById(USER_1.getId())).thenReturn(Optional.of(USER_1));
        when(giftDao.getById(2)).thenReturn(Optional.of(GIFT_CERTIFICATE_2));
        when(orderDao.insert(ORDER_21)).thenReturn(ORDER_2);
        UserDto userDto = orderService.saveByUser(1, List.of(new GiftCertificateDto(2)));
        User actual = new User(userDto.getId(), userDto.getName());

        assertEquals(USER_1, actual);
    }

    @Test
    void getOrderByUser() {
        EntityPage entityPage = new EntityPage(0, 2);
        when(orderDao.getOrdersByUser(USER_1.getId(), entityPage)).thenReturn(createPaginateResult());

        PaginationResult<OrderDto> actual = orderService.getOrderByUser(USER_1.getId(), entityPage);
        assertEquals(createExpectedResult(), actual);
    }

    private PaginationResult<Order> converter(PaginationResult<OrderDto> paginationResult) {
        PaginationResult<Order> actual = new PaginationResult<>();
        List<Order> ordersList = paginationResult.getRecords()
                .stream()
                .map(OrderConvert::toEntity)
                .collect(Collectors.toList());
        actual.setRecords(ordersList);
        actual.setPage(paginationResult.getPage());
        return actual;
    }

    private PaginationResult<Order> createPaginateResult() {
        List<Order> ordersList = Arrays.asList(ORDER_1, ORDER_2);
        Page page = new Page(1, 2, 2, 4);
        PaginationResult<Order> paginationResult = new PaginationResult<>();
        paginationResult.setPage(page);
        paginationResult.setRecords(ordersList);
        return paginationResult;
    }

    private PaginationResult<OrderDto> createExpectedResult() {
        List<Order> ordersList = Arrays.asList(ORDER_1, ORDER_2);
        List<OrderDto> orderDtos = ordersList
                .stream()
                .map(OrderConvert::toDto)
                .collect(Collectors.toList());
        Page page = new Page(1, 2, 2, 4);
        PaginationResult<OrderDto> paginationResult = new PaginationResult<>();
        paginationResult.setPage(page);
        paginationResult.setRecords(orderDtos);
        return paginationResult;
    }


}