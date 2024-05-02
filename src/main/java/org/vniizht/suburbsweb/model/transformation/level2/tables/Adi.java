package org.vniizht.suburbsweb.model.transformation.level2.tables;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vniizht.suburbsweb.model.transformation.level2.L2Common;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "rawdl2",
        name = "l2_prig_adi")
@Getter
@Setter
@ToString(callSuper=true)
public class Adi extends L2Common {
    private String      surname;
    private String      initials;
    private String      passport;
    private String      benefit_doc;
    private String      militreq_date;
    private Character   employee_cat;
    private String      bilgroup_code;
    private Character   bilgroup_secur;
    private String      employee_unit;
    private String      dependent_surname;
    private String      dependent_initials;
    private String      snils;
}