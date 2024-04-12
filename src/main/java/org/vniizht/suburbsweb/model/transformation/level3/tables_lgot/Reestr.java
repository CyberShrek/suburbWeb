package org.vniizht.suburbsweb.model.transformation.level3.tables_lgot;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_lgot_reestr")
@Getter
@Setter
public class Reestr {
    private short           yymm;
    private Date            date_zap;
    private int             part_zap;
    private int             request_num;
    private long            idnum;
    private String          list;
    private String          kodbl;
    private int             p1;
    private String          p2;
    private String          p3;
    private char            p4;
    private char            p5;
    private char            p6;
    private String          p7;
    private String          p8;
    private String          p9;
    private String          p10;
    private String          p11;
    private String          p12;
    private char            p13;
    private String          p14;
    private String          p15;
    private int             p16;
    private char            p17;
    private String          p18;
    private int             p19;
    private char            p20;
    private String          p21;
    private String          p22;
    private String          p23;
    private String          p24;
    private String          p25;
    private String          p26;
    private float           p27;
    private float           p28;
    private String          p29;
    private String          p30;
    private String          p31;
    private String          p32;
    private int             p33;
    private char            deleted;

}
