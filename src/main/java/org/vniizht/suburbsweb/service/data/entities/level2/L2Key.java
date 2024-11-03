package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@ToString
abstract public class L2Key implements Serializable {
    @Id
    public Long        idnum;
    public Integer     yyyymm;
    @Column(name = "request_date")
    public Date requestDate;
}
