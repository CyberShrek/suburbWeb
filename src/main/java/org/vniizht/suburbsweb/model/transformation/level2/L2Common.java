package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Date;

@MappedSuperclass
@ToString
@Getter
@Setter
abstract public class L2Common implements Serializable {
    @Id
    private Long        idnum;
    private Integer     yyyymm;
    @Column(name = "request_date")
    private Date        requestDate;
}
