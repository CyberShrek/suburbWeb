package org.vniizht.suburbsweb.service.data.entities.handbook;

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
        name = "dork")
@Getter
@Setter
@ToString
public class Dor {
    @Id
    private Integer         id;
    @Column(name = "d_nomd2") private String    nom2;
    @Column(name = "d_nom3")  private String    nom3;
    @Column(name = "d_kod")   private Character kod;
    @Column(name = "d_kodvc") private Character vc;
    @Column(name = "d_datan") private Date datan;
    @Column(name = "d_datak") private Date datak;
    @Column(name = "d_datani") private Date datani;
    @Column(name = "d_dataki") private Date dataki;
//    @Column(name = "d_nom3")  private String    kodg;
}
