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
@Table(schema = "prig",
        name = "sublx")
@Getter
@Setter
@ToString
public class Sublx {
    @Id
    private Integer        id;

    @Column(name = "code_lg_gvc")
    private Integer       gvc;
    private String         lg;
    private Date        datan;
    private Date        datak;
}

