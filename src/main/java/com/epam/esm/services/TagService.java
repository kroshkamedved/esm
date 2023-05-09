package com.epam.esm.services;

import com.epam.esm.domain.Tag;
import com.epam.esm.exception.EmptySetException;
import com.epam.esm.exception.EntityCannotBeSaved;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(@Qualifier("MYSQL") TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag storeTag(Tag tag) {
        try {
            return tagRepository.storeTag(tag);
        } catch (Exception e) {
            throw new EntityCannotBeSaved("Resource cannot be saved (" + e.getMessage() + ")");
        }
    }

    public Tag getTag(long id) {
        return tagRepository.getTag(id).orElseThrow(
                () -> new EntityNotFoundException("Requested resource not found (tag id =" + id + ")")
        );
    }

    public void deleteTag(long id) {
        tagRepository.deleteTag(id);
    }

    public List<Tag> getAll() {
        List<Tag> tags = tagRepository.getAll();
        if (tags.isEmpty()) {
            throw new EmptySetException("there are no elements in the list");
        }
        return tags;
    }
}
