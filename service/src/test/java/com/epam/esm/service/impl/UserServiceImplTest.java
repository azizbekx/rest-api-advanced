package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.mapper.UserConvert;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.repository.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final User USER_1 = new User(1, "Tomas");
    private final User USER_2 = new User(2, "Tony");
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private OrderDao orderDao = Mockito.mock(OrderDao.class);
    @Mock
    private UserDao userDao = Mockito.mock(UserDao.class);

    @Test
    void getAll() {
        EntityPage entityPage = new EntityPage(0, 2);
        when(userDao.list(entityPage)).thenReturn(createPaginateResult());

        PaginationResult<User> actual = converter(userService.getAll(entityPage));
        PaginationResult<User> expected = createPaginateResult();
        assertEquals(expected, actual);
    }

    @Test
    void getById() {
        when(userDao.getById(USER_1.getId())).thenReturn(Optional.of(USER_1));

        User actual = UserConvert.toEntity(userService.getById(USER_1.getId()));
        assertEquals(USER_1, actual);
    }

    private PaginationResult<User> converter(PaginationResult<UserDto> paginationResult) {
        PaginationResult<User> actual = new PaginationResult<>();
        List<User> userList = paginationResult.getRecords()
                .stream()
                .map(UserConvert::toEntity)
                .collect(Collectors.toList());
        actual.setRecords(userList);
        actual.setPage(paginationResult.getPage());
        return actual;
    }

    private PaginationResult<User> createPaginateResult() {
        List<User> userList = Arrays.asList(USER_1, USER_2);
        Page page = new Page(1, 2, 2, 4);
        PaginationResult<User> paginationResult = new PaginationResult<>();
        paginationResult.setPage(page);
        paginationResult.setRecords(userList);
        return paginationResult;
    }

}