package com.epam.esm.service;

import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.pagination.PaginationResult;

public interface BasicService<T> {
    PaginationResult<T> getAll(EntityPage entityPage);

    T getById(long id);

    T insert(T t);

    boolean deleteById(long id);
}
