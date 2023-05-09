package com.epam.esm.repositories;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag storeTag(Tag tag);

    Optional<Tag> getTag(long id);

    void deleteTag(long id);

    List<Tag> getAll();

    boolean saveTagsForCertificate(List<Tag> tags, long id);

    List<Tag> getLinkedTags(long certificateId);
}
