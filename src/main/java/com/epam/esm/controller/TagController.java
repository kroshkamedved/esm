package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST Controller for basic operation with Tag
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    /**
     * return tag with {id}
     *
     * @param id requested tag id
     *           if it is present in the db
     * @return Tag with corresponding id.
     * @throws com.epam.esm.exception.EntityNotFoundException if id is not valid.
     */
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Tag fetchById(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }

    /**
     * Return all tags;
     *
     * @return List of Tag
     */
    @GetMapping()
    @ResponseStatus(OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Tag> fetchAll() {
        return tagService.getAll();
    }

    /**
     * Create new Tag in db from request body tag
     *
     * @param tag tag from request body
     * @return created tag from db with id which was assigned to tag by db.
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.storeTag(tag);
    }

    /**
     * Delete tag from db with corresponding {id} if id is valid.
     *
     * @param id valid tag id.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteById(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
    }
}

