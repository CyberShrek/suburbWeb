package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_itog")
@Getter
@Setter
public class Itog {
    private long            idnum;
    private int             request_num;
    private short           yymm;
    private int             k_bil;
    private int             nom_mar;
    private int             nom_bil;
    private int             nom_dat;
    private Date            date_zap;
    private int             part_zap;
    private Date            date_beg;
    private Date            date_end;
    private Date            date_pr;
    private char            term_dor;
    private String          term_pos;
    private String          term_trm;
    private short           agent;
    private short           subagent;
    private short           chp;
    private int             stp;
    private short           stp_reg;
    private String          train_num;
    private int             kol_bil;
    private long            plata;
    private long            poteri;
    private long            perebor;
    private long            nedobor;
    private long            kom_sbor;
    private long            kom_sbor_vz;

}
