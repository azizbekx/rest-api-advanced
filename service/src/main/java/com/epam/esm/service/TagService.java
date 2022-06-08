package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

public interface TagService extends BasicService<TagDto> {

    TagDto getByName(String name);

    TagDto getTopUsedOfUser(long userId);
}
