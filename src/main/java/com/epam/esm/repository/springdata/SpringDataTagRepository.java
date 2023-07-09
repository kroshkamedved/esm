package com.epam.esm.repository.springdata;

import com.epam.esm.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTagRepository extends JpaRepository<Tag,Long> {
}
