package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UtilJdbc {

    @Autowired private JdbcTemplate jdbcTemplate;

    public Long getLong(String query) {
        return jdbcTemplate.queryForObject(query, Long.class);
    }

    public int getInt(String query) {
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public double getDouble(String query) {
        return jdbcTemplate.queryForObject(query, Double.class);
    }

    public boolean getBoolean(String query) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(query, Boolean.class));
    }

    public String getString(String query) {
        return jdbcTemplate.queryForObject(query, String.class);
    }
}
