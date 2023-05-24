package com.epam.esm.repository.mappers;


import com.epam.esm.domain.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagMapper implements RowMapper { //TODO fix raw use and same comment as for CertificateMapper
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = Tag.builder() //TODO just return this
                .id(rs.getLong("tag_id"))
                .name(rs.getString("tag_name"))
                .build();
        return tag;
    }
}
