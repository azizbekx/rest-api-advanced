package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.Optional;

/**
 * Interface {@code GiftCertificateDao} describes operations for working with gift certificate object in database tables.
 */

public interface GiftCertificateDao extends BasicDao<GiftCertificate> {
    GiftCertificate update(GiftCertificate giftCertificate);

    boolean deleteFromTag(long id);

    boolean deleteFromOrder(long id);

    Optional<GiftCertificate> getByName(String name);

}
