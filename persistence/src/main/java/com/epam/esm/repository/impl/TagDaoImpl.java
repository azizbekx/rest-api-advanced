package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.PaginationDao;
import com.epam.esm.repository.TagDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class TagDaoImpl extends PaginationDao<Tag> implements TagDao {
    private static final String SELECT_MOST_USED_TAG_WITH_HIGHEST_COST_OF_ORDER =
            "SELECT t FROM GiftCertificate g INNER JOIN g.tags t "
                    + "WHERE g.id IN (SELECT o.giftCertificate.id FROM Order o "
                    + "WHERE o.user.id = :userId) GROUP BY t.id ORDER BY COUNT(t.id) DESC";
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    GiftCertificateDao giftDao;

    @Autowired
    public TagDaoImpl() {
        super(Tag.class);
    }

    @Override
    public Optional<Tag> getById(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    public Optional<Tag> getByName(String name) {
        return entityManager
                .createQuery("select t from Tag t where t.name = :name", Tag.class)
                .setParameter("name", name)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    @Transactional
    public Tag insert(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    @Transactional
    public boolean remove(Tag tag) {
        deleteRemovedTag(tag.getId());
        entityManager.remove(tag);
        return entityManager.find(Tag.class, tag.getId()) == null;
    }


    @Override
    public boolean deleteRemovedTag(long id) {
        return entityManager
                .createNativeQuery("DELETE FROM giftsystem.gift_certificates_tags WHERE tag_id=:tag_id")
                .setParameter("tag_id", id)
                .executeUpdate() > 0;
    }

    @Override
    public Optional<Tag> getTopUsedWithHighestCostOfOrder(long userId) {
        return entityManager.createQuery(SELECT_MOST_USED_TAG_WITH_HIGHEST_COST_OF_ORDER, Tag.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst();

    }
}
