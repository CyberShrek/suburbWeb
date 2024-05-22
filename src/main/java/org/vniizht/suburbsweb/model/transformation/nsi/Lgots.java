package org.vniizht.suburbsweb.model.transformation.nsi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(schema = "nsi",
        name = "lgots")
@Getter
@Setter
@ToString
public class Lgots {
    @Id
    private Integer         id;
    private String        lgot;
    private Date        datand;
    private Date        datakd;
}
