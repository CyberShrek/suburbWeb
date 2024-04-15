package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Date;

@MappedSuperclass
@ToString
@Getter
@Setter
abstract public class AbstractParent implements Serializable {
    @Id
    private Long        idnum;
    private Long        id;
    private Integer     yyyymm;
    private Date        request_date;
    private Integer     request_num;
    private String      term_pos;
    private Character   term_dor;
    private String      term_trm;
    private Short       arxiv_code;
    private Short       reply_code;
    private Short       doc_num;
    private String      ticket_ser;
    private Integer     ticket_num;
}
