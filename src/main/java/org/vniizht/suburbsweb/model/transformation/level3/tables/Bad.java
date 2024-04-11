package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_bad")
@Getter
@Setter
public class Bad {
    private int             marshr;
    private int             st1;
    private int             st2;
    private int             rst;
    private short           reg;
    private short           dor;
    private short           lin;
    private Date            date_zap;
    private int             part_zap;
    private long            idnum;
}
