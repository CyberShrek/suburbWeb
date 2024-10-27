package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "rawdl2",
        name = "l2_prig_adi")
@ToString(callSuper=true)
public class PrigAdi extends L2Common {
    public Character   employee_cat;
    public Character   bilgroup_secur;
    public String      bilgroup_code;
    public String      benefit_doc;
    public String      employee_unit;
    public String      surname;
    public String      initials;
    public String      dependent_surname;
    public String      dependent_initials;
    public String      snils;
}
