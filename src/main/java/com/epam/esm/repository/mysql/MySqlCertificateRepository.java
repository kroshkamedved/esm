package com.epam.esm.repository.mysql;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.dto.dto.GiftCertificateDTO;
import com.epam.esm.repository.mappers.CertificateMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Repository
@Qualifier("MYSQL")
@AllArgsConstructor
public class MySqlCertificateRepository implements CertificateRepository {

    private static final String GET_ALL_CERTIFICATES = "SELECT * FROM certificates";
    private static final String SELECT_BY_ID = "SELECT * FROM certificates where id = ?";
    private static final String DELETE_BY_ID = "DELETE FROM certificates WHERE id = ?";
    private static final String STORE_NEW_CERTIFICATE = "INSERT INTO certificates (`name`, `description`, `price`, `duration`) VALUES (?, ?, ?, ?);";
    private static final String SELECT_ALL_CERTIFICATES_WITH_TAGNAME = "SELECT * FROM certificates c \n" +
            "            JOIN certificates_to_tags ctt\n" +
            "            ON ctt.certificate_id = c.id\n" +
            "            LEFT JOIN tags t \n" +
            "            ON t.tag_id = ctt.tag_id\n" +
            "            WHERE t.tag_name like \"%\" :tagName \"%\"";
    private static final String SELECT_ALL_CERTIFICATES_WITH_NAME = "select * from certificates" +
            " where name like \"%\" ? \"%\"";
    private static final String SELECT_ALL_CERTIFICATES_WITH_DESCRIPTION = "select * from certificates" +
            " where description like \"%\" ? \"%\"";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final CertificateMapper certificateMapper;

    @Override//TODO add URI header
    public GiftCertificate storeCertificate(GiftCertificate certificate) {
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
    public Optional<GiftCertificate> getCertificate(long id) {
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

        if (certificate.getId() == 0l || !(getCertificate(certificate.getId()).isPresent())) {
            throw new EntityNotFoundException("ID attribute is absent or not valid");
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
            mapSqlParameterSource.addValue("duration", certificate.getPrice());
        }

        if (mapSqlParameterSource.getValues().size() == 1) {
            return false;
        }

        return namedParameterJdbcTemplate.update(stringJoiner.toString(), mapSqlParameterSource) > 0;
    }

    @Override
    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(GET_ALL_CERTIFICATES, certificateMapper);
    }

    @Override
    public List<GiftCertificate> getAllCertificatesWithTagName(String tagName) {
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("tagName", tagName);
        return namedParameterJdbcTemplate.query(SELECT_ALL_CERTIFICATES_WITH_TAGNAME, parameterSource, certificateMapper);
    }

    @Override
    public List<GiftCertificate> getAllCertificatesWithName(String name) {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES_WITH_NAME, certificateMapper, name);
    }

    @Override
    public List<GiftCertificate> getAllCertificatesWithDescription(String description) {
        return jdbcTemplate.query(SELECT_ALL_CERTIFICATES_WITH_DESCRIPTION, certificateMapper, description);
    }

}
