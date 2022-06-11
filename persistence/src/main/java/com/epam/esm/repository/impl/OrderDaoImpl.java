package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.OrderDao;
import com.epam.esm.repository.pagination.GeneratePaginate;
import com.epam.esm.repository.pagination.PaginationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderDaoImpl extends PaginationDao<Order> implements OrderDao {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private GeneratePaginate<Order> orderGeneratePaginate;

    @Autowired
    public OrderDaoImpl() {
        super(Order.class);
    }

    @Override
    public Optional<Order> getById(long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
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
    public PaginationResult<Order> getOrdersByUser(long userId, EntityPage page) {
        int lastPageNumber;
        Long totalRecords;
        List<Order> orderList;

        TypedQuery<Long> countQuery =
                entityManager.createQuery("SELECT COUNT (e.id) FROM Order e where e.user.id = :userId", Long.class)
                        .setParameter("userId", userId);
        totalRecords = countQuery.getSingleResult();


        if (totalRecords % page.getSize() == 0) {
            lastPageNumber = (int) (totalRecords / page.getSize());
        } else {
            lastPageNumber = (int) (totalRecords / page.getSize() + 1);
        }
        TypedQuery<Order> query =
                entityManager.createQuery("SELECT e FROM Order e " +
                                " WHERE e.user.id = :userId ORDER BY e.id ", Order.class)
                        .setParameter("userId", userId);

        query.setFirstResult((page.getPage() - 1) * page.getSize());
        query.setMaxResults(page.getSize());
        orderList = query.getResultList();
        return orderGeneratePaginate.
                generatePaginate(page, lastPageNumber, totalRecords, orderList);

    }
}
