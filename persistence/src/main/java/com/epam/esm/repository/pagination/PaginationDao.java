package com.epam.esm.repository.pagination;

import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public abstract class PaginationDao<T> {
    protected final Class<T> entity;
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public PaginationDao(Class<T> entity) {
        this.entity = entity;
    }

    public PaginationResult<T> list(EntityPage page) {
        int lastPageNumber;
        Long totalRecords;
        List<T> entityList;

        TypedQuery<Long> countQuery =
                entityManager.createQuery("SELECT COUNT (e.id) FROM " + entity.getSimpleName() + " e", Long.class);
        totalRecords = countQuery.getSingleResult();


        if (totalRecords % page.getSize() == 0) {
            lastPageNumber = (int) (totalRecords / page.getSize());
        } else {
            lastPageNumber = (int) (totalRecords / page.getSize() + 1);
        }

        TypedQuery<T> query =
                entityManager.createQuery("SELECT e FROM " + entity.getSimpleName() + " e " +
                        " ORDER BY e.id ", entity);

        query.setFirstResult((page.getPage() - 1) * page.getSize());
        query.setMaxResults(page.getSize());
        entityList = query.getResultList();

        PaginationResult<T> result = new PaginationResult<>();
        Page presentPage = new Page();
        presentPage.setCurrentPageNumber(page.getPage());
        presentPage.setPageSize(page.getSize());
        presentPage.setLastPageNumber(lastPageNumber);
        presentPage.setTotalRecords(totalRecords);
        result.setPage(presentPage);
        result.setRecords(entityList);

        return result;
    }
}
