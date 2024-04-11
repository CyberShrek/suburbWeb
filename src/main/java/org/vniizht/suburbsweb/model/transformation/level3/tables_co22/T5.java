package org.vniizht.suburbsweb.model.transformation.level3.tables_co22;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prig_co22_t5")
@Getter
@Setter
public class T5 {
    private float           yymm;
    private String          p1;
    private String          p2;
    private int             p3;
    private int             p4;
    private int             p5;
    private float           p6;
    private float           p7;
    private float           p8;
    private float           p9;
    private float           p10;
}
