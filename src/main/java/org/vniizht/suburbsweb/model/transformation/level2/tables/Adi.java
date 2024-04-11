package org.vniizht.suburbsweb.model.transformation.level2.tables;

import lombok.Getter;
import lombok.Setter;
import org.vniizht.suburbsweb.model.transformation.level2.AbstractParent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "l2_prig_adi")
@Getter
@Setter
public class Adi extends AbstractParent {
    private String  surname;
    private String  initials;
    private String  passport;
    private String  benefit_doc;
    private String  militreq_date;
    private char    employee_cat;
    private String  bilgroup_code;
    private byte    bilgroup_secur;
    private String  employee_unit;
    private String  dependent_surname;
    private String  dependent_initials;
    private String  dependent_snils;
}