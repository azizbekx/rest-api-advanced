package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.entity.creteria.GiftSearchCriteria;
import com.epam.esm.pagination.PaginationResult;

public interface GiftCertificateService extends BasicService<GiftCertificateDto> {
    GiftCertificateDto update(long id, GiftCertificateDto giftCertificateDto);

    PaginationResult<GiftCertificateDto> getWithFilter(GiftSearchCriteria searchCriteria, EntityPage entityPage);

}
