package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.pagination.PaginationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public class GiftCertificateDaoImpl extends PaginationDao<GiftCertificate> implements GiftCertificateDao {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public GiftCertificateDaoImpl() {
        super(GiftCertificate.class);
    }


    @Override
    public Optional<GiftCertificate> getById(long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    public GiftCertificate insert(GiftCertificate gift) {
        entityManager.persist(gift);
        return gift;
    }

    // Gift _ 1 [ tag_1, tag_2 ]
    // Gift _ 2 [tag_1, tag_3]
    // removed Gift _ 1 [
    // have tag _ 1 , tag _ 2
    @Override
    public boolean remove(GiftCertificate giftCertificate) {
        deleteFromTag(giftCertificate.getId());
        deleteFromOrder(giftCertificate.getId());
        entityManager.remove(giftCertificate);
        return entityManager.find(GiftCertificate.class, giftCertificate.getId()) == null;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftC) {
        return entityManager.merge(giftC);
    }

    @Override
    public boolean deleteFromTag(long id) {
        return entityManager.createNativeQuery("DELETE FROM gift_certificates_tags WHERE gift_certificate_id=:gift_certificate_id")
                .setParameter("gift_certificate_id", id)
                .executeUpdate() > 0;
    }

    @Override
    public boolean deleteFromOrder(long id) {
        return entityManager.createNativeQuery("DELETE FROM orders_gift_certificates WHERE gift_certificate_id=:gift_certificate_id")
                .setParameter("gift_certificate_id", id)
                .executeUpdate() > 0;
    }

    @Override
    public Optional<GiftCertificate> getByName(String name) {
        return entityManager.createQuery("select g from GiftCertificate g where g.name = :name", GiftCertificate.class)
                .setParameter("name", name)
                .getResultList()
                .stream().findFirst();
    }

}