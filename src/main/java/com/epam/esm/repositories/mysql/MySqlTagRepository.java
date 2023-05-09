package com.epam.esm.repositories.mysql;

import com.epam.esm.domain.Tag;
import com.epam.esm.repositories.TagRepository;
import com.epam.esm.repositories.mappers.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Qualifier("MYSQL")
public class MySqlTagRepository implements TagRepository {
    private static final String SELECT_BY_ID = "SELECT tag_id , tag_name FROM TAGS WHERE tag_id = ?";
    private static final String SELECT_ALL_TAGS = "SELECT tag_id , tag_name FROM TAGS";
    private static final String STORE_NEW_TAG = "insert into tags (tag_name) values(?)";
    private static final String DELETE = "delete from tags where tag_id=?";
    public static final String ADD_TAG_TO_CERTIFICATE = "insert into certificates_to_tags(certificate_id, tag_id) " +
            "values(?, ?)";
    private static final String SELECT_BY_CERTIFICATE_ID = "select t.tag_id, t.tag_name " +
            "from certificates_to_tags ct join tags t " +
            "on t.tag_id =  ct.tag_id " +
            "where certificate_id=?";

    private final JdbcTemplate jdbcTemplate;
    private final TagMapper tagMapper;

    @Autowired
    public MySqlTagRepository(JdbcTemplate jdbcTemplate, TagMapper tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagMapper = tagMapper;
    }

    @Override
    public Tag storeTag(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(STORE_NEW_TAG, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, tag.getName());
                return preparedStatement;
            }, keyHolder);
        } catch (DataIntegrityViolationException duplicationError) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag = " + tag.getName() + " already present in the DB", duplicationError);
        }
        tag.setId((keyHolder.getKey()).longValue());
        return tag;
    }

    @Override
    public Optional<Tag> getTag(long id) {
        return jdbcTemplate.
                query(SELECT_BY_ID, tagMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public void deleteTag(long id) {
        jdbcTemplate.update(DELETE, id);
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(SELECT_ALL_TAGS, tagMapper);
    }

    @Override
    public boolean saveTagsForCertificate(List<Tag> tags, long id) {
        boolean newTagLinked = false;
        List<Tag> tagsInDb = getAll();
        List<Tag> currentLinkedTags = getLinkedTags(id);
        for (Tag tag : tags) {
            Optional<Tag> tagForLink = tagsInDb.stream()
                    .filter(dbTag -> dbTag.getName().equals(tag.getName()) || dbTag.getId() == tag.getId())
                    .findAny();

            Tag tagForSaving = (tagForLink.isEmpty()) ? (storeTag(tag)) : tagForLink.get();

            if (currentLinkedTags.contains(tagForSaving)) {
                continue;
            }
            linkTagForCertificate(tagForSaving, id);
            newTagLinked = true;
        }
        return newTagLinked;
    }

    @Override
    public List<Tag> getLinkedTags(long certificateId) {
        return jdbcTemplate.query(SELECT_BY_CERTIFICATE_ID, tagMapper, certificateId);
    }

    private void linkTagForCertificate(Tag tag, long id) {
        jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE, id, tag.getId());
    }
}
