package com.epam.esm.service.impl;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.entity.creteria.GiftSearchCriteria;
import com.epam.esm.mapper.GiftConverter;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.filter.GiftFilterDao;
import com.epam.esm.repository.impl.GiftCertificateDaoImpl;
import com.epam.esm.repository.impl.TagDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {
    private final Tag TAG_1 = new Tag(1, "tag_1");
    private final Tag TAG_2 = new Tag(2, "tag_2");
    private final Tag TAG_3 = new Tag(3, "tag_3");
    private final GiftCertificate GIFT_CERTIFICATE_1 =
            new GiftCertificate(1, "Gift_1", "For holiday", new BigDecimal("10.9"), 3,
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    new HashSet<>(Arrays.asList(TAG_1, TAG_2)));
    private final GiftCertificate GIFT_CERTIFICATE_2 =
            new GiftCertificate(2, "Gift_2", "For good work", new BigDecimal("20.9"), 3,
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    new HashSet<>(Arrays.asList(TAG_1, TAG_3)));
    private final GiftCertificate GIFT_CERTIFICATE_3 =
            new GiftCertificate(3, "Gift_3", "For occupation mars", new BigDecimal("30.9"), 3,
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    new HashSet<>(Arrays.asList(TAG_1, new Tag("tag_100"))));
    private final GiftCertificate GIFT_CERTIFICATE_31 = new GiftCertificate("Gift_3",
            "For occupation mars",
            new BigDecimal("30.2"), 3,
            LocalDateTime.parse("2022-03-29T06:12:15.156"),
            LocalDateTime.parse("2022-03-29T06:12:15.156"));
    private final GiftCertificate GIFT_CERTIFICATE_4 =
            new GiftCertificate(4, "Gift_4", "For mars", new BigDecimal("30.9"), 3,
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    LocalDateTime.parse("2022-03-29T06:12:15.156"),
                    new HashSet<>(Arrays.asList(TAG_1, new Tag("tag_100"))));
    private final GiftCertificate GIFT_CERTIFICATE_41 = new GiftCertificate("For updated mars",
            LocalDateTime.parse("2022-03-29T06:12:15.156"),
            LocalDateTime.parse("2022-03-29T06:12:15.156"));
    private final GiftCertificate GIFT_CERTIFICATE_42 = new GiftCertificate(4, "Gift_4", "For updated mars", new BigDecimal("30.9"), 3,
            LocalDateTime.parse("2022-03-29T06:12:15.156"),
            LocalDateTime.parse("2022-03-29T06:12:15.156"),
            new HashSet<>(Arrays.asList(TAG_1, new Tag("tag_100"))));
    @Mock
    private GiftCertificateDaoImpl giftDao = Mockito.mock(GiftCertificateDaoImpl.class);
    @Mock
    private GiftFilterDao giftFilterDao = Mockito.mock(GiftFilterDao.class);
    @Mock
    private TagDaoImpl tagDao = Mockito.mock(TagDaoImpl.class);
    @InjectMocks
    private GiftCertificateServiceImpl giftService;

    @Test
    public void getById_test() {
        when(giftDao.getById(GIFT_CERTIFICATE_1.getId())).thenReturn(Optional.of(GIFT_CERTIFICATE_1));
        GiftCertificate actual = GiftConverter.toEntity(giftService.getById(GIFT_CERTIFICATE_1.getId()));

        assertEquals(GIFT_CERTIFICATE_1, actual);
    }

    @Test
    public void getAllTest() {
        EntityPage entityPage = new EntityPage(0, 2);
        when(giftDao.list(entityPage)).thenReturn(createPaginateResult());

        PaginationResult<GiftCertificate> actual = converter(giftService.getAll(entityPage));
        PaginationResult<GiftCertificate> expected = createPaginateResult();
        assertEquals(expected, actual);
    }

    @Test
    public void update_test() {
        GiftCertificateDto giftDto = GiftConverter.toDto(GIFT_CERTIFICATE_41);
        long id = 1;
        when(giftDao.getById(id)).thenReturn(Optional.of(GIFT_CERTIFICATE_4));
        when(giftDao.update(GIFT_CERTIFICATE_42)).thenReturn(GIFT_CERTIFICATE_42);

        GiftCertificate actual = GiftConverter.toEntity(giftService.update(id, giftDto));
        assertEquals(GIFT_CERTIFICATE_4, actual);

    }

    @Test
    public void insert_test() {

        GIFT_CERTIFICATE_31.setTags(createReqTagsWithIds());
        when(tagDao.getByName("tag_1")).thenReturn(Optional.of(TAG_1));
        when(tagDao.getByName("tag_100")).thenReturn(Optional.empty());
        when(giftDao.insert(GIFT_CERTIFICATE_31)).thenReturn(GIFT_CERTIFICATE_3);

        GiftCertificateDto reqGift = GiftConverter.toDto(GIFT_CERTIFICATE_31);
        GiftCertificate actual = GiftConverter.toEntity(giftService.insert(reqGift));

        assertEquals(GIFT_CERTIFICATE_3, actual);
    }

    @Test
    void doFilter_test() {
        EntityPage page = new EntityPage();
        GiftSearchCriteria giftSearchCriteria = new GiftSearchCriteria(
                "ift", "For", null, null, null, null, null);
        when(giftFilterDao.findAllWithFilters(giftSearchCriteria, page)).thenReturn(createPaginateResult());

        PaginationResult<GiftCertificateDto> actualDto = giftService.getWithFilter(giftSearchCriteria, page);
        PaginationResult<GiftCertificate> actual = converter(actualDto);
        assertEquals(createPaginateResult(), actual);
    }

    @Test
    void delete_test() {
        when(giftDao.getById(2)).thenReturn(Optional.of(GIFT_CERTIFICATE_2));
        when(giftDao.remove(GIFT_CERTIFICATE_2)).thenReturn(true);

        boolean success = giftService.deleteById(2L);
        assertTrue(success);
    }

    private PaginationResult<GiftCertificate> converter(PaginationResult<GiftCertificateDto> paginationResult) {
        PaginationResult<GiftCertificate> actual = new PaginationResult<>();
        List<GiftCertificate> giftList = paginationResult.getRecords()
                .stream()
                .map(GiftConverter::toEntity)
                .collect(Collectors.toList());
        actual.setRecords(giftList);
        actual.setPage(paginationResult.getPage());
        return actual;
    }

    private PaginationResult<GiftCertificate> createPaginateResult() {
        List<GiftCertificate> giftList = Arrays.asList(GIFT_CERTIFICATE_1, GIFT_CERTIFICATE_2);
        Page page = new Page(1, 2, 2, 4);
        PaginationResult<GiftCertificate> paginationResult = new PaginationResult<>();
        paginationResult.setPage(page);
        paginationResult.setRecords(giftList);
        return paginationResult;
    }

    private Set<Tag> createReqTagsWithIds() {
        return new HashSet<>(Arrays.asList(
                TAG_1,
                new Tag("tag_100")
        ));
    }

}