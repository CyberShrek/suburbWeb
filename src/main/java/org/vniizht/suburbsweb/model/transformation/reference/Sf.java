package org.vniizht.suburbsweb.model.transformation.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(schema = "nsi",
        name = "sf")
@Getter
@Setter
@ToString
public class Sf {
    @Id
    private Integer         id;
    @Column(name = "sf_vid")
    private Integer        vid;
    @Column(name = "sf_kodokato")
    private String       okato;
    @Column(name = "sf_datan")
    private Date         datan;
    @Column(name = "sf_datak")
    private Date         datak;
}
