package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.TagDao;
import com.epam.esm.repository.pagination.PaginationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class TagDaoImpl extends PaginationDao<Tag> implements TagDao {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    GiftCertificateDao giftDao;

    @Autowired
    public TagDaoImpl() {
        super(Tag.class);
    }

    /*
    Native SQL version
    select t from tags t
    join gift_certificates_tags gct on t.id = gct.tag_id
    join gift_certificates gc on gc.id = gct.gift_certificate_id
    where gc.id in
          (select gc2.id from gift_certificates gc2
                         join orders_gift_certificates ogc on gc2.id = ogc.gift_certificate_id
                         join orders o on o.id = ogc.order_id
                         where o.user_id = :userId)
    group by t.id order by count(t.id) desc;
     */
    private static final String SELECT_TOP_USED_TAG_WITH_HIGHEST_COST_OF_ORDERS =
            "select t from GiftCertificate g " +
                    "join g.tags t " +
                    "where g.id in (select g2.id from Order o join o.giftCertificates g2 " +
                    "where o.user.id = :userId) group by t.id order by count(t.id) desc";

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

    // Gift _ 1 [ tag_1, tag_2 ]
    // Gift _ 2 [tag_1, tag_3]
    // removed Gift _ 1 [
    // have tag _ 1 , tag _ 2
    @Override
    public boolean deleteRemovedTag(long id) {
        return entityManager
                .createNativeQuery("DELETE FROM gift_certificates_tags WHERE tag_id=:tag_id")
                .setParameter("tag_id", id)
                .executeUpdate() > 0;
    }

    @Override
    public Optional<Tag> getTopUsedWithHighestCostOfOrder(long userId) {
        return entityManager.createQuery(SELECT_TOP_USED_TAG_WITH_HIGHEST_COST_OF_ORDERS, Tag.class)
                .setParameter("userId", userId)
                .getResultList()
                .stream()
                .findFirst();

    }
}
