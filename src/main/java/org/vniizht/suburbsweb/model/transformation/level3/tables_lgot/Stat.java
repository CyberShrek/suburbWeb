package org.vniizht.suburbsweb.model.transformation.level3.tables_lgot;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_lgot_stat")
@Getter
@Setter
public class Stat {
    private short           yymm;
    private String          list;
    private String          dor;
    private int             kol_zap;
    private int             kol_del;
    private int             kol_raz;
    private int             kol_abon;
    private int             kol_ab_k;
    private float           plata;
    private float           poteri;
    private int             kol_porc;
    private Date            date_zap;
}
