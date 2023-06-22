package com.epam.esm.controller;

import com.epam.esm.domain.Tag;
import com.epam.esm.hateoas.assembler.TagModelAssembler;
import com.epam.esm.pagination.Page;
import com.epam.esm.pagination.PageRequest;
import com.epam.esm.pagination.assembler.TagPageAssembler;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.epam.esm.pagination.Sort;

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
    private final TagPageAssembler tagPageAssembler;
    private final TagModelAssembler tagModelAssembler;

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
    public EntityModel<Tag> fetchById(@PathVariable("id") Long id) {
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
    public Page<Tag> fetchAll(@RequestParam(defaultValue = "1", name = "page") String page,
                              @RequestParam(defaultValue = "2", name = "pageSize") String pageSize,
                              @RequestParam(defaultValue = "ASC", name = "sortOrder") Sort sortOrder) {
        PageRequest pageRequest = new PageRequest(Integer.parseInt(page), Integer.parseInt(pageSize), sortOrder);
        int totalRecords = tagService.getTotalRecords();
        List<Tag> tags = tagService.getAll(pageRequest);
        return tagPageAssembler.pageOf(tags, pageRequest, totalRecords);
    }

    /**
     * Create new Tag in db from request body tag
     *
     * @param tag tag from request body
     * @return created tag from db with id which was assigned to tag by db.
     */
    @PostMapping
    @ResponseStatus(CREATED)
    public EntityModel<Tag> createTag(@RequestBody Tag tag) {
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
    public EntityModel<Tag> fetchMostWidelyUsedTagOfTheBestClient() {
        return tagModelAssembler.toModel(tagService.getFavouriteBestClienTag());
    }
}

