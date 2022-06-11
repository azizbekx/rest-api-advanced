package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.pagination.PaginationResult;

import java.util.List;

public interface OrderService extends BasicService<OrderDto> {
    UserDto saveByUser(long id, List<GiftCertificateDto> giftDtos);

    PaginationResult<OrderDto> getOrderByUser(long userId, EntityPage entityPage);
}
