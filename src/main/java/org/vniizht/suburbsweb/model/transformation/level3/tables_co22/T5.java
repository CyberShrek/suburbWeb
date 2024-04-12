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
    private int             yymm;
    private String          p1;
    private String          p2;
    private int             p3;
    private int             p4;
    private int             p5;
    private long            p6;
    private long            p7;
    private long            p8;
    private long            p9;
    private long            p10;
}
