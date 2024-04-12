package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_times")
@Getter
@Setter
public class Times {
    private Date            date;
    private String          time;
    private double          time2;
    private String          dann;
    private String          oper;
    private Date            date_zap;
    private int             part_zap;
    private long            rezult;
    private long            min_id;
    private long            max_id;
    private long            min_id_svod;
    private long            max_id_svod;
    private String          shema;
    private String          libr;
    private String          itog;
    private int             yyyymm;

}
