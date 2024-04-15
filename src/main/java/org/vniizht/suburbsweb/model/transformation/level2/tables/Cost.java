package org.vniizht.suburbsweb.model.transformation.level2.tables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vniizht.suburbsweb.model.transformation.level2.AbstractParent;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "zzz_rawdl2_old",
        name = "l2_prig_cost",
        indexes = @Index(name = "l2_prig_cost_indid", columnList = "id, doc_num"))
@IdClass(Cost.Identifier.class)
@ToString(callSuper=true)
@Getter
@Setter
public class Cost extends AbstractParent {
    @Id
    private Short       doc_reg;
    private Short       route_num;
    private Short       route_distance;
    private Long        tariff_sum;
    private Long        department_sum;
    private String      departure_station;
    private String      arrival_station;
    private String      region_code;
    private Character   tarif_type;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        private Long    idnum;
        private Short   doc_reg;
    }
}
