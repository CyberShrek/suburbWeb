package org.vniizht.suburbsweb.model.transformation.level2.tables;

import lombok.Getter;
import lombok.Setter;
import org.vniizht.suburbsweb.model.transformation.level2.AbstractParent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "l2_prig_cost")
@Getter
@Setter
public class Cost extends AbstractParent {
    @Id
    private short   doc_reg;
    private short   route_num;
    private short   route_distance;
    private long    tariff_sum;
    private long    department_sum;
    private String  departure_station;
    private String  arrival_station;
    private String  region_code;
    private char    tarif_type;
}
