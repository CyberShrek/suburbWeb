package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_dats")
@Getter
@Setter
public class Dats {
    private int             nom_dat;
    private int             plus_dat;
    private int             kpas_day;
    private long            idnum;
    private Date            date_zap;
    private int             part_zap;
}
