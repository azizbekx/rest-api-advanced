package com.epam.esm.repository.impl;

import com.epam.esm.entity.User;
import com.epam.esm.repository.UserDao;
import com.epam.esm.repository.pagination.PaginationDao;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl extends PaginationDao<User> implements UserDao {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> getById(long id) {
        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public User insert(User object) {
        throw new NotYetImplementedException();
    }

    @Override
    public boolean remove(User user) {
        throw new NotYetImplementedException();
    }
}
