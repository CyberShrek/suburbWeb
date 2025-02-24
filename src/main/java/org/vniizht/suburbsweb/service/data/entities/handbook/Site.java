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
        name = "site")
@Getter
@Setter
@ToString
public class Site {
    @Id
    private Integer         id;
    private String      idsite;
    private String       tsite;
    private String         gos;
    private Date         datan;
    private Date         datak;
}
