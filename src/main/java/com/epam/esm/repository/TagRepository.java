package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import com.epam.esm.pagination.PageRequest;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    Tag createTag(Tag tag);

    Optional<Tag> fetchTag(long id);

    void deleteTag(long id);

    List<Tag> fetchAll(PageRequest pageRequest);

    boolean saveTagsForCertificate(List<Tag> tags, long id);

    List<Tag> fetchLinkedTags(long certificateId);

    int getTotalRecords();
}
