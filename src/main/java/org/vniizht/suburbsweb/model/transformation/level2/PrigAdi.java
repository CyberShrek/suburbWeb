package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "zzz_rawdl2",
        name = "l2_prig_adi")
@ToString(callSuper=true)
@Getter
@Setter
public class PrigAdi extends L2Common {
    private Character   employee_cat;
    private String      bilgroup_code;
}
