package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_peregoni")
@Getter
@Setter
public class Peregoni {
    private int             peregon;
    private short           dor;
    private short           lin;
    private int             st1;
    private int             st2;
    private String          name;
    private short           rasst;
    private Date            date_zap;
    private int             part_zap;

}
