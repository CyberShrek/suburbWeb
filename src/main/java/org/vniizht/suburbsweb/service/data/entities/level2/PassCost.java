package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.AllArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_cost")
@IdClass(PassCost.Identifier.class)
@ToString(callSuper=true)
public class PassCost extends L2Common {
    @Id public Short       sum_code;
    @Id public String      cnt_code;
    @Id public String      dor_code;
    @Id public Character   paymenttype;
    public Float           department_sum;
    public Float           sum_nde;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        public Long        idnum;
        public Short       sum_code;
        public String      cnt_code;
        public String      dor_code;
        public Character   paymenttype;

        public Identifier() {}
    }
}
