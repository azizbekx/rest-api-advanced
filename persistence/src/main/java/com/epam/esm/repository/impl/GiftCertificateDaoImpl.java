package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateDao;
import com.epam.esm.repository.PaginationDao;
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
        try {
            entityManager.persist(gift);
            return gift;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean remove(GiftCertificate giftCertificate) {
        deleteRemovedGiftCertificate(giftCertificate.getId());
        entityManager.remove(giftCertificate);
        return entityManager.find(GiftCertificate.class, giftCertificate.getId()) == null;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftC) {
        return entityManager.merge(giftC);
    }

    @Override
    public boolean deleteRemovedGiftCertificate(long id) {
        return entityManager.createNativeQuery("DELETE FROM giftsystem.gift_certificates_tags WHERE gift_certificate_id=:gift_certificate_id")
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