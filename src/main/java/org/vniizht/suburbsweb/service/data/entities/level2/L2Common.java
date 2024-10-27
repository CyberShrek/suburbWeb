package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.ToString;

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
