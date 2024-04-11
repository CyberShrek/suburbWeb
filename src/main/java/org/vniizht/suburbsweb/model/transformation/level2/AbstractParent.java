package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Getter
@Setter
abstract public class AbstractParent {
    @Id
    private long    idnum;
    private long    id;
    private int     yyyymm;
    private Date    request_date;
    private int     request_num;
    private String  term_pos;
    private char    term_dor;
    private String  term_trm;
    private short   arxiv_code;
    private short   reply_code;
    private short   doc_num;
    private String  ticket_ser;
    private int     ticket_num;
}
