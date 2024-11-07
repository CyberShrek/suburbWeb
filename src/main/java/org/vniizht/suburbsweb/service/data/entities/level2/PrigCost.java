package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "rawdl2",
        name = "l2_prig_cost")
@IdClass(PrigCost.Identifier.class)
@ToString(callSuper=true)
public class PrigCost extends L2Key {
    @Id
    public Short       doc_reg;
    public Short       route_num;
    public Short       route_distance;
    public Long        tariff_sum;
    public Long        department_sum;
    public String      departure_station;
    public String      arrival_station;
    public String      region_code;
    public Character   tarif_type;

    @ManyToOne
    @JoinColumn(name="idnum", insertable = false, updatable = false)
    @Getter
    @Setter
    private PrigMain main;

    @AllArgsConstructor
    @EqualsAndHashCode
    static public class Identifier implements Serializable {
        public Long    idnum;
        public Short   doc_reg;

        public Identifier() {}
    }
}
