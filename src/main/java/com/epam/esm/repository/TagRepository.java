package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TagRepository {
    Tag createTag(Tag tag);

    Optional<Tag> fetchTag(long id);

    void deleteTag(long id);


    boolean saveTagsForCertificate(Set<Tag> tags, long id);

    List<Tag> fetchLinkedTags(long certificateId);

    int getTotalRecords();
}
