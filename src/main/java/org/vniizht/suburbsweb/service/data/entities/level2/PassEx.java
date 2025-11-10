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
@Table(schema = "prig_bekap",
        name = "l2_pass_ex")
@IdClass(PassEx.Identifier.class)
@ToString(callSuper=true)
public class PassEx extends L2Key {
    @Id
    public Short       npp;
    public String      ticket_ser;
    public Integer     ticket_num;
    public String      lgot_info;
    public String      nomlgud;
    public String      last_name;
    public String      first_name;
    public String      patronymic;
    public String      snils;

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
