package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "zzz_rawdl2",
        name = "l2_pass_cost")
@IdClass(PassCost.Identifier.class)
@ToString(callSuper=true)
public class PassCost extends L2Key {
    @Id public Short       sum_code;
    @Id public String      cnt_code;
    @Id public String      dor_code;
    @Id public Character   paymenttype;
    public Float           sum_te; // department_sum
    public Float           sum_nde;

    @ManyToOne
    @JoinColumn(name = "idnum", referencedColumnName = "idnum", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassMain main;

    @AllArgsConstructor
    @EqualsAndHashCode
    static public class Identifier implements Serializable {
        public Long        idnum;
        public Short       sum_code;
        public String      cnt_code;
        public String      dor_code;
        public Character   paymenttype;

        public Identifier() {}
    }
}
