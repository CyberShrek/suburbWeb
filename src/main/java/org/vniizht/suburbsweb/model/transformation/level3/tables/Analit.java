package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_analit")
@Getter
@Setter
public class Analit {
    private short           yymm;
    private Date            date_zap;
    private int             part_zap;
    private Date            date;
    private char            term_dor;
    private short           agent;
    private short           chp;
    private short           reg;
    private String          par_name;
    private String          anal_rasch;
    private String          anal_vid_bil;
    private char            anal_oper;
    private char            train_category;
    private long            kol_bil;
    private long            plata;
    private long            poteri;
    private long            kol_pas;
    private long            pass_km;
}
