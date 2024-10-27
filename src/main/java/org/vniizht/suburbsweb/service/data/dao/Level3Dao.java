package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Level3Dao {

    private static final String
            schema    = "l3_prig",
            mainTable = "prig_co22_t1";

    @Autowired private JdbcTemplate jdbcTemplate;

    public Long getLastId(){
        return jdbcTemplate.queryForObject("SELECT max(idnum) FROM " + schema + "." + mainTable, Long.class);
    }
}
