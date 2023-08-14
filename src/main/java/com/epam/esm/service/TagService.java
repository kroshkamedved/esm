package com.epam.esm.service;

import com.epam.esm.domain.Order;
import com.epam.esm.domain.Tag;
import com.epam.esm.domain.User;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.springdata.SpringDataTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SpringDataTagRepository springDataTagRepository;


    public Tag storeTag(Tag tag) {
        return springDataTagRepository.save(tag);
    }

    public Tag getTag(long id) {
        return springDataTagRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Requested resource not found (tag id =" + id + ")", Error.TagNotFound)
        );
    }

    public void deleteTag(long id) {
        springDataTagRepository.deleteById(id);
    }

    public Page<Tag> getAll(Pageable pageable) {
        return springDataTagRepository.findAll(pageable);
    }

    public Tag getFavouriteBestClientTag() {
        User bestClient = userRepository.fetchUserWithHighestOrdersCost();
        List<Order> orders = orderRepository.fetchUserOrders(bestClient.getId());
        Map<Tag, Long> map = orders.stream()
                .flatMap(o -> o.getOrderCertificates().stream())
                .flatMap(g -> tagRepository.fetchLinkedTags(g.getId()).stream())
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));
        return Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
