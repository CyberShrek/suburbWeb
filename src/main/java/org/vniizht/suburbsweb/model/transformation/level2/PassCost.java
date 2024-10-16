package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_cost")
@IdClass(PassCost.Identifier.class)
@Getter
@Setter
@ToString(callSuper=true)
public class PassCost extends L2Common {
    @Id private Short       sum_code;
    @Id private String      cnt_code;
    @Id private String      dor_code;
    @Id private Character   paymenttype;
    private Float           sum_nde;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        private Long        idnum;
        private Short       sum_code;
        private String      cnt_code;
        private String      dor_code;
        private Character   paymenttype;

        public Identifier() {}
    }
}
