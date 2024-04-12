package org.vniizht.suburbsweb.model.transformation.level3.tables_co22;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prig_co22_t4")
@Getter
@Setter
public class T4 {
    private int             yymm;
    private String          p1;
    private String          p2;
    private int             p3;
    private short           p4;
    private String          p5;
    private String          p6;
    private long            p7;
    private long            p8;
    private short           p9;
}
