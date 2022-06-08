package com.epam.esm.repository.filter;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.entity.creteria.GiftSearchCriteria;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class GiftFilterDao {
    @PersistenceContext
    EntityManager em;

    public PaginationResult<GiftCertificate> findAllWithFilters(GiftSearchCriteria searchCriteria, EntityPage entityPage) {
        int lastPageNumber;
        long totalRecords;
        List<GiftCertificate> entityList;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> giftRoot = query.from(GiftCertificate.class);
        Predicate predicate = getPredicate(builder, searchCriteria, giftRoot);

        query.where(predicate);

        //ordering according to sort param
        orderBy(query, builder, giftRoot, entityPage);

        // getting results
        TypedQuery<GiftCertificate> typedQuery = em.createQuery(query);
        // Count total records
        totalRecords = typedQuery.getResultList().size();
        // setting page number and size
        typedQuery.setFirstResult((entityPage.getPage() - 1) * entityPage.getSize());
        typedQuery.setMaxResults(entityPage.getSize());
        entityList = typedQuery.getResultList();

        if (totalRecords % entityPage.getSize() == 0) {
            lastPageNumber = (int) (totalRecords / entityPage.getSize());
        } else {
            lastPageNumber = (int) (totalRecords / entityPage.getSize() + 1);
        }
        PaginationResult<GiftCertificate> result = new PaginationResult<>();

        Page presentPage = new Page();
        presentPage.setCurrentPageNumber(entityPage.getPage());
        presentPage.setPageSize(entityPage.getSize());
        presentPage.setLastPageNumber(lastPageNumber);
        presentPage.setTotalRecords(totalRecords);
        result.setPage(presentPage);
        result.setRecords(entityList);

        return result;
    }

    private void orderBy(CriteriaQuery<GiftCertificate> query,
                         CriteriaBuilder builder,
                         Root<GiftCertificate> giftRoot, EntityPage entityPage) {
        if (entityPage.getSortDir().equalsIgnoreCase("asc")) {
            query.orderBy(builder.asc(giftRoot.get(entityPage.getSortBy())));
        } else {
            query.orderBy(builder.desc(giftRoot.get(entityPage.getSortBy())));
        }
    }

    private Predicate getPredicate(
            CriteriaBuilder builder,
            GiftSearchCriteria searchCriteria,
            Root<GiftCertificate> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(searchCriteria.getName())) {
            predicates.add(
                    builder.like(root.get("name"),
                            "%" + searchCriteria.getName() + "%"));
        }
        if (Objects.nonNull(searchCriteria.getDescription())) {
            predicates.add(
                    builder.like(root.get("description"),
                            "%" + searchCriteria.getDescription() + "%")
            );
        }
        if (Objects.nonNull(searchCriteria.getPrice())) {
            predicates.add(
                    builder.like(root.get("price"),
                            "%" + searchCriteria.getPrice() + "%")
            );
        }
        if (Objects.nonNull(searchCriteria.getDuration())) {
            predicates.add(
                    builder.equal(root.get("duration"),
                            searchCriteria.getDuration())
            );
        }
        if (Objects.nonNull(searchCriteria.getCreate_date())) {
            String create_date = searchCriteria.getCreate_date();
            Expression<String> dateStringExpr =
                    builder.function("DATE_FORMAT",
                            String.class,
                            root.get("createDate"),
                            builder.literal("'%d/%m/%Y'"));
            predicates.add(
                    builder.like(builder.lower(dateStringExpr), "%" + create_date.toLowerCase() + "%"));

        }
        if (Objects.nonNull(searchCriteria.getLast_update_date())) {
            String create_date = searchCriteria.getLast_update_date();
            Expression<String> dateStringExpr =
                    builder.function("DATE_FORMAT",
                            String.class,
                            root.get("lastUpdateData"),
                            builder.literal("'%d/%m/%Y'"));
            predicates.add(
                    builder.like(
                            builder.lower(dateStringExpr), "%" + create_date.toLowerCase() + "%"));
        }
        if (Objects.nonNull(searchCriteria.getTag_name())) {
            String tag_name = searchCriteria.getTag_name();
            if (tag_name.contains(",")) {
                String[] tag = tag_name.split(",");
                for (String s : tag) {
                    predicates.add(
                            builder.equal(
                                    root.join("tags").get("name"), s));
                }
            } else {
                predicates.add(
                        builder.equal(
                                root.join("tags").get("name"), searchCriteria.getTag_name()));
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
