package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_lgot")
@IdClass(PassLgot.Identifier.class)
@ToString(callSuper=true)
public class PassLgot extends L2Key {
    @Id
    public Short       npp;
    public String      benefit_prigcode;
    public String      bilgroup_code;
    public Character   employee_cat;
    public String      employee_unit;
    public String      document_num;


    @OneToOne
    @JoinColumn(name = "idnum", referencedColumnName = "idnum", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassMain main;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        public Long  idnum;
        public Short npp;

        public Identifier() {}
    }
}
