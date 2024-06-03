package org.vniizht.suburbsweb.model.transformation.level3.lgot;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@Builder
@ToString
public class Stat {
    private Short           yymm;
    private String          list;
    private String          dor;
    private Integer         kol_zap;
    private Integer         kol_del;
    private Integer         kol_raz;
    private Integer         kol_abon;
    private Integer         kol_ab_k;
    private Float           plata;
    private Float           poteri;
    private Integer         kol_porc;
    private Date            date_zap;
}
