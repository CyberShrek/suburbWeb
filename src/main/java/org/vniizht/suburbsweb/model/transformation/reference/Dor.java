package org.vniizht.suburbsweb.model.transformation.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "nsi",
        name = "dor")
@Getter
@Setter
@ToString
public class Dor {
    @Id
    private Integer         id;
    private String          nomd3;
    private Character       kodd;
    private Character       vc;
    private String          kodg;
}
