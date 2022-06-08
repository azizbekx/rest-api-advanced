package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.creteria.EntityPage;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.mapper.TagConverter;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PaginationResult;
import com.epam.esm.repository.impl.TagDaoImpl;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDaoImpl tagDao;

    @Override
    public PaginationResult<TagDto> getAll(EntityPage entityPage) {

        PaginationResult<Tag> tagList = tagDao.list(entityPage);
        if (tagList.getRecords().isEmpty()) {
            throw new ResourceNotFoundException();
        }
        //converting Tag object to TagDto object
        List<TagDto> tagDtos = tagList.getRecords()
                .stream()
                .map(TagConverter::toDto)
                .collect(Collectors.toList());

        //collect in right format
        return new PaginationResult<>(
                new Page(
                        tagList.getPage().getCurrentPageNumber(),
                        tagList.getPage().getLastPageNumber(),
                        tagList.getPage().getPageSize(),
                        tagList.getPage().getTotalRecords()),
                tagDtos
        );
    }

    @Override
    public TagDto getById(long id) {
        Optional<Tag> tag = tagDao.getById(id);
        if (tag.isEmpty()) {
            throw new ResourceNotFoundException(id);
        }
        return TagConverter.toDto(tag.get());
    }

    @Override
    public TagDto getByName(String name) {
        Optional<Tag> tag = tagDao.getByName(name);
        if (tag.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return TagConverter.toDto(tag.get());
    }

    @Override
    public TagDto getTopUsedOfUser(long userId) {
        Optional<Tag> optionalTag = tagDao.getTopUsedWithHighestCostOfOrder(userId);
        if (optionalTag.isEmpty()) {
            throw new ResourceNotFoundException(userId);
        }
        return TagConverter.toDto(optionalTag.get());
    }

    @Override
    public TagDto insert(TagDto tagDto) {
        Optional<Tag> tag = tagDao.getByName(tagDto.getName());
        if (tag.isPresent()) {
            throw new DuplicateEntityException();
        }
        return TagConverter.toDto(tagDao.insert(TagConverter.toEntity(tagDto)));
    }

    @Override
    public boolean deleteById(long id) {
        Optional<Tag> tag = tagDao.getById(id);
        if (tag.isEmpty()) {
            throw new ResourceNotFoundException(id);
        }
        return tagDao.remove(tag.get());
    }

}
