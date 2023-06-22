package com.epam.esm.repository.mappers.impl;

import com.epam.esm.domain.GiftCertificate;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Component
public class CertificateRowMapperImpl implements RowMapper<GiftCertificate> {
    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GiftCertificate.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .price(rs.getBigDecimal("price"))
                .createDate(rs.getTimestamp("create_time").toInstant())
                .lastUpdateDate(rs.getTimestamp("update_time").toInstant())
                .duration(rs.getInt("duration"))
                .build();
    }
}