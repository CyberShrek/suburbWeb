package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "rawdl2",
        name = "l2_prig_adi")
@ToString(callSuper=true)
@Getter
@Setter
public class PrigAdi extends L2Common {
    private Character   employee_cat;
    private Character   bilgroup_secur;
    private String      bilgroup_code;
    private String      benefit_doc;
    private String      employee_unit;
    private String      surname;
    private String      initials;
    private String      dependent_surname;
    private String      dependent_initials;
    private String      snils;
}
