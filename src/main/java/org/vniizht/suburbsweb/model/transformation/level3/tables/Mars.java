package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_mars")
@Getter
@Setter
public class Mars {
    private int             nom_mar;
    private int             sto;
    private int             stn;
    private short           srasst;
    private short           sto_zone;
    private short           stn_zone;
    private int             sti;
    private short           sti_zone;
    private short           nom;
    private short           reg;
    private int             marshr;
    private short           mcd;
    private int             st1;
    private int             st2;
    private short           rst;
    private short           dor;
    private short           lin;
    private long            d_plata;
    private long            d_poteri;
    private short           otd;
    private short           dcs;
    private int             peregon;
    private long            idnum;
    private Date            date_zap;
    private int             part_zap;

}
