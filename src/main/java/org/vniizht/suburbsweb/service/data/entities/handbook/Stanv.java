package org.vniizht.suburbsweb.service.data.entities.handbook;

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
    private String         otd;
    private String          sf;
    private String         gos;
    private String    kodokato;
    private Date        datand;
    private Date        datakd;
    private Date        datani;
    private Date        dataki;
}
