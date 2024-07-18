package org.vniizht.suburbsweb.model.transformation.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(schema = "nsi",
        name = "plagn")
@Getter
@Setter
@ToString
public class Plagn {
    @Id
    private Integer         id;
    private String     idplagn;
    private String          vr;
    private String         gos;
    private Date         datan;
    private Date         datak;
}
