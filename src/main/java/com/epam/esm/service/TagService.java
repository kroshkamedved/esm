package com.epam.esm.service;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exception.EmptySetException;
import com.epam.esm.exception.EntityCannotBeSaved;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.pagination.PageRequest;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;


    public Tag storeTag(Tag tag) {
        try {
            if (tag.getName() != null) {
                return tagRepository.createTag(tag);
            } else throw new EntityCannotBeSaved("tag should have a name");
        } catch (Exception e) {
            throw new EntityCannotBeSaved("Resource cannot be saved (" + e.getMessage() + ")");
        }
    }

    public Tag getTag(long id) {
        return tagRepository.fetchTag(id).orElseThrow(
                () -> new EntityNotFoundException("Requested resource not found (tag id =" + id + ")", Error.TagNotFound)
        );
    }

    public void deleteTag(long id) {
        tagRepository.deleteTag(id);
    }

    public List<Tag> getAll(PageRequest pageRequest) {
        List<Tag> tags = tagRepository.fetchAll(pageRequest);
        if (tags.isEmpty()) {
            throw new EmptySetException("there are no elements in the list");
        }
        return tags;
    }

    public Tag getFavouriteBestClienTag() {
        User bestClient = userRepository.fetchUserWithHighestOrdersCost();
        List<Order> orders = orderRepository.fetchUserOrders(bestClient.getId());
        Map<Tag, Long> map = orders.stream()
                .flatMap(o -> o.getOrderCertificates().stream())
                .flatMap(g -> tagRepository.fetchLinkedTags(g.getId()).stream())
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));
        return Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    public int getTotalRecords() {
        return tagRepository.getTotalRecords();
    }
}
