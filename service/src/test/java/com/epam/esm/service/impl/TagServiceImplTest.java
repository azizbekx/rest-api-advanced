package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.mapper.TagConverter;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.impl.TagDaoImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
    private static final Tag TAG_1 = new Tag(1, "tag_1");
    private static final Tag TAG_2 = new Tag(2, "tag_2");
    private static final Tag TAG_3 = new Tag(3, "tag_3");
    private static final Tag TAG_4 = new Tag(4, "tag_4");
    @Mock
    private TagDaoImpl tagDao = Mockito.mock(TagDaoImpl.class);
    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void getByName() {
        when(tagDao.getByName("tag_2")).thenReturn(Optional.of(TAG_2));

        Tag actual = TagConverter.toEntity(tagService.getByName("tag_2"));
        assertEquals(TAG_2, actual);
    }

    @Test
    public void getById_test() {
        when(tagDao.getById(TAG_1.getId())).thenReturn(Optional.of(TAG_1));
        Tag actual = TagConverter.toEntity(tagService.getById(TAG_1.getId()));
        assertEquals(TAG_1, actual);
    }

    @Test
    public void getAllTest() {
        EntityPage page = new EntityPage(0, 2);
        when(tagDao.list(page)).thenReturn(createPaginateResult());
        PaginationResult<Tag> actual = converter(tagService.getAll(page));
        PaginationResult<Tag> expected = createPaginateResult();
        assertEquals(expected, actual);
    }

    @Test
    public void insert_test() {
        Tag newTag = new Tag("tag_4");
        when(tagDao.insert(newTag)).thenReturn(TAG_4);

        TagDto actualDto = tagService.insert(TagConverter.toDto(newTag));
        Tag actual = TagConverter.toEntity(actualDto);

        assertEquals(TAG_4, actual);
    }

    @Test
    void delete_test() {
        when(tagDao.getById(3L)).thenReturn(Optional.of(TAG_3));
        when(tagDao.remove(TAG_3)).thenReturn(true);

        boolean success = tagService.deleteById(3L);
        assertTrue(success);
    }

    private PaginationResult<Tag> converter(PaginationResult<TagDto> paginationResult) {
        PaginationResult<Tag> actual = new PaginationResult<>();
        List<Tag> tagList = paginationResult.getRecords()
                .stream()
                .map(TagConverter::toEntity)
                .collect(Collectors.toList());
        actual.setRecords(tagList);
        actual.setPage(paginationResult.getPage());
        return actual;
    }

    private PaginationResult<Tag> createPaginateResult() {
        List<Tag> tagList = Arrays.asList(TAG_1, TAG_2);
        Page page = new Page(1, 2, 2, 4);
        PaginationResult<Tag> paginationResult = new PaginationResult<>();
        paginationResult.setPage(page);
        paginationResult.setRecords(tagList);
        return paginationResult;
    }
}