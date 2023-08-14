package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.hateoas.assembler.TagModelAssembler;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    private final TagModelAssembler tagModelAssembler;
    private final PagedResourcesAssembler<Tag> pagedResourcesAssembler;

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
    public TagModel fetchById(@PathVariable("id") Long id) {
        return tagModelAssembler.toModel(tagService.getTag(id));
    }

    /**
     * Return all tags;
     *
     * @return List of Tag
     */
    @GetMapping()
    @ResponseStatus(OK)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedModel<TagModel> fetchAll(Pageable pageable) {
        return pagedResourcesAssembler.toModel(tagService.getAll(pageable), tagModelAssembler);
    }

    /**
     * Create new Tag in db from request body tag
     *
     * @param tag tag from request body
     * @return created tag from db with id which was assigned to tag by db.
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public TagModel createTag(@RequestBody Tag tag) {
        return tagModelAssembler.toModel(tagService.storeTag(tag));
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

    /**
     * Get the most widely used tag of a user with the highest cost of all orders
     *
     * @return tag
     */
    @GetMapping("/widelyUsedBestClientTag")
    public TagModel fetchMostWidelyUsedTagOfTheBestClient() {
        return tagModelAssembler.toModel(tagService.getFavouriteBestClientTag());
    }
}

