package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Level3Data {

    private static final String
            schema    = "l3_prig",
            mainTable = "prig_co22_t1";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long getLastId(){
        return jdbcTemplate.queryForObject("SELECT max(idnum) FROM " + schema + "." + mainTable, Long.class);
    }

    @PostConstruct
    private void test(){
        System.out.println(getLastId());
    }
}
