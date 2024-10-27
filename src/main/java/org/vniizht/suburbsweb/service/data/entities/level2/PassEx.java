package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.AllArgsConstructor;
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
public class PassEx extends L2Common {
    @Id
    public Short       npp;
    public String      ticket_ser;
    public Short       ticket_num;
    public String      lgot_info;
    public String      nomlgud;
    public String      last_name;
    public String      first_name;
    public String      patronymic;
    public String      snils;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        public Long  idnum;
        public Short npp;

        public Identifier() {}
    }
}
