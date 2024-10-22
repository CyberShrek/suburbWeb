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
abstract public class L2Common implements Serializable {
    @Id
    public Long        idnum;
    public Integer     yyyymm;
    public Date request_date;
}
