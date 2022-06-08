package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;

public interface TagDao extends BasicDao<Tag> {
    Optional<Tag> getByName(String name);

    boolean deleteRemovedTag(long id);

    Optional<Tag> getTopUsedWithHighestCostOfOrder(long userId);
}
