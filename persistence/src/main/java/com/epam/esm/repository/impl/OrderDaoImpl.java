package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.repository.PaginationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderDaoImpl extends PaginationDao<Order> implements OrderDao {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public OrderDaoImpl() {
        super(Order.class);
    }

    @Override
    public Optional<Order> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Order insert(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public boolean remove(Order order) {
        return false;
    }

    @Override
    public List<Order> getOrdersByUser(long userId) {
        return entityManager.createQuery("select o from Order o where o.user.id = :userId", Order.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
