package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.Logger;

@Service
public class Level3Data {

    private static final String
            schema    = "l3_prig",
            mainTable = "prig_co22_t1";

    @Autowired private Logger logger;
    @Autowired private JdbcTemplate jdbcTemplate;

    public Long getLastId(){
        return jdbcTemplate.queryForObject("SELECT max(idnum) FROM " + schema + "." + mainTable, Long.class);
    }
}
