package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_ex")
@IdClass(PassEx.Identifier.class)
@ToString(callSuper=true)
@Getter
@Setter
public class PassEx extends L2Common {
    @Id
    private Short   npp;
    private String  lgot_info;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        private Short npp;

        public Identifier() {}
    }
}
