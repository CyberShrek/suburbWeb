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
        name = "stanv")
@Getter
@Setter
@ToString
public class Stanv {
    @Id
    private Integer         id;
    private String        stan;
    private String        nopr;
    private Character      dor;
    private String         gos;
    private Date        datand;
    private Date        datakd;
}
