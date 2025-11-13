package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_main_upd")
@ToString(callSuper=true)
public class PassMainUpd extends L2Key {
    public Character no_use;

    @OneToOne
    @JoinColumn(name = "idnum", referencedColumnName = "idnum", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassMain main;
}