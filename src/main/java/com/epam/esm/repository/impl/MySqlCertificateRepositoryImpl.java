package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.GiftCertificatePriceOnly;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.Error;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.mappers.impl.CertificateRowMapperImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MySqlCertificateRepositoryImpl implements CertificateRepository {
    private static final String SELECT_ALL_CERTIFICATES = "SELECT * FROM certificates";
    private static final String SELECT_BY_ID = "SELECT * FROM certificates where id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM certificates WHERE id = ?";
    private static final String STORE_NEW_CERTIFICATE = "INSERT INTO certificates (`name`, `description`, `price`, `duration`) VALUES (?, ?, ?, ?);";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapperImpl certificateMapper;
    private final EntityManager entityManager;

    @Override
    public GiftCertificate createCertificate(GiftCertificate certificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(STORE_NEW_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, certificate.getName());
            preparedStatement.setString(2, certificate.getDescription());
            preparedStatement.setBigDecimal(3, certificate.getPrice());
            preparedStatement.setInt(4, certificate.getDuration());
            return preparedStatement;
        }, keyHolder);
        certificate.setId((keyHolder.getKey()).longValue());
        return certificate;
    }

    @Override
    public Optional<GiftCertificate> fetchCertificate(long id) {
        return jdbcTemplate
                .query(SELECT_BY_ID, certificateMapper, id)
                .stream()
                .findAny();
    }

    @Override
    public void deleteCertificate(long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public boolean updateCertificate(GiftCertificateDTO certificate) {

        if (certificate.getId() == 0L || fetchCertificate(certificate.getId()).isEmpty()) {
            throw new EntityNotFoundException("ID attribute is absent or not valid", Error.GiftCertificateNotFound);
        }
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", certificate.getId());


        String prefix = "update certificates c set ";
        String suffix = " where c.id =:id";
        StringJoiner stringJoiner = new StringJoiner(",", prefix, suffix);

        if (certificate.getName() != null) {
            stringJoiner.add("c.name =:name");
            mapSqlParameterSource.addValue("name", certificate.getName());
        }
        if (certificate.getDescription() != null) {
            stringJoiner.add("c.description =:description");
            mapSqlParameterSource.addValue("description", certificate.getDescription());
        }
        if (certificate.getPrice() != null) {
            stringJoiner.add("c.price =:price");
            mapSqlParameterSource.addValue("price", certificate.getPrice());
        }
        if (certificate.getDuration() != null) {
            stringJoiner.add("c.duration =:duration");
            mapSqlParameterSource.addValue("duration", certificate.getDuration());
        }

        if (mapSqlParameterSource.getValues().size() == 1) {
            return false;
        }

        return namedParameterJdbcTemplate.update(stringJoiner.toString(), mapSqlParameterSource) > 0;
    }

    @Override
    public List<GiftCertificate> fetchAll() {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES, certificateMapper);
    }

    @Override
    public List<GiftCertificate> fetchAllParametrized(String name,
                                                      String description,
                                                      String sortOrder,
                                                      Optional<String> sortByDate,
                                                      Optional<String> sortByName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);

        ArrayList<Predicate> predicates = new ArrayList<>();
        if (name != null && description != null) {
            // Case 1: Both name and description are specified
            Predicate nameLike = criteriaBuilder.like(root.get("name"), MessageFormat.format("%{0}%", name));
            Predicate descriptionLike = criteriaBuilder.like(root.get("description"), MessageFormat.format("%{0}%", description));
            predicates.add(criteriaBuilder.and(nameLike, descriptionLike));
        } else if (name != null) {
            // Case 2: Only name is specified
            predicates.add(criteriaBuilder.like(root.get("name"), MessageFormat.format("%{0}%", name)));
        } else if (description != null) {
            // Case 3: Only description is specified
            Predicate nameLikeDescriptionValue = criteriaBuilder.like(root.get("name"), MessageFormat.format("%{0}%", description));
            Predicate descriptionLikeDescriptionValue = criteriaBuilder.like(root.get("description"), MessageFormat.format("%{0}%", description));
            predicates.add(criteriaBuilder.or(nameLikeDescriptionValue, descriptionLikeDescriptionValue));
        }

        if (sortOrder != null) {
            boolean desc = sortOrder.equals("DESC");
            List<Order> orders = new ArrayList<>();
            if (desc) {
                if (sortByDate.isPresent()) {
                    orders.add(criteriaBuilder.desc(root.get("createDate")));
                }
                if (sortByName.isPresent()) {
                    orders.add(criteriaBuilder.desc(root.get("name")));
                }
            } else {
                if (sortByDate.isPresent()) {
                    orders.add(criteriaBuilder.asc(root.get("createDate")));
                }
                if (sortByName.isPresent()) {
                    orders.add(criteriaBuilder.asc(root.get("name")));
                }
            }
            criteriaQuery.orderBy(orders);
        }

        criteriaQuery.select(root).where(predicates.toArray(Predicate[]::new));
        Query query = entityManager.createQuery(criteriaQuery);
        List resultList = query.getResultList();
        return resultList;
    }

    @Override
    @Transactional
    public void updateCertificatePrice(GiftCertificatePriceOnly certificatePriceDto) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, certificatePriceDto.getId());
        certificate.setPrice(certificatePriceDto.getPrice());
    }

    //It's work, but I don't like this solution, maybe you could give me a hint
    @Override
    public List<GiftCertificate> fetchAllCertificatesWithTagId(Set<Long> tagsIds) {
        String prefix = "select * from certificates c" +
                " join (select ctt.certificate_id, count(ctt.certificate_id) as cnt" +
                " from certificates_to_tags ctt" +
                " where ctt.tag_id in (";
        String suffix = ") group by ctt.certificate_id having cnt >=" + tagsIds.size() + ") s1 on s1.certificate_id = c.id";

        StringJoiner joiner = new StringJoiner(",", prefix, suffix);
        for (Long id : tagsIds) {
            joiner.add(id.toString());
        }
        Query nativeQuery = entityManager.createNativeQuery(joiner.toString(), GiftCertificate.class);
        return nativeQuery.getResultList();
    }

    @Override
    public int countAllCertificatesWithRequestedTags(Set<Long> tagsIds) {
        String prefix = "select count(*) from certificates c" +
                " join (select ctt.certificate_id, count(ctt.certificate_id) as cnt" +
                " from certificates_to_tags ctt" +
                " where ctt.tag_id in (";
        String suffix = ") group by ctt.certificate_id having cnt >=" + tagsIds.size() + ") s1 on s1.certificate_id = c.id";

        StringJoiner joiner = new StringJoiner(",", prefix, suffix);
        for (Long id : tagsIds) {
            joiner.add(id.toString());
        }
        return ((Number) entityManager.createNativeQuery(joiner.toString(), Integer.class).getSingleResult()).intValue();
    }
}
