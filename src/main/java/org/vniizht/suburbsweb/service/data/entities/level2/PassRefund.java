package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Table(schema = "prig_bekap",
        name = "l2_pass_refund")
@ToString(callSuper=true)
public class PassRefund extends L2Key {
    @Id  public Long       idnum;
    public Character flg_retpret;

    @OneToOne
    @JoinColumn(name = "idnum", referencedColumnName = "idnum", insertable = false, updatable = false)
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassMain main;
}
