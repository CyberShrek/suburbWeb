package org.vniizht.suburbsweb.model.transformation.nsi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "nsi",
        name = "sf")
@Getter
@Setter
@ToString
public class Sf {
    @Id
    private Integer         id;
    private String sf_kodokato;
    private String     sf_kod2;
}
