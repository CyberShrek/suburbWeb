package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

@Service
public class ReportsJdbc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SqlRowSet fetchReport() {
        return jdbcTemplate.queryForRowSet("SELECT * FROM magic.log LIMIT 100");
    }
}
