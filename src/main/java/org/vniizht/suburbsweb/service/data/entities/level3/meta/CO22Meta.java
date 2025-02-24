package org.vniizht.suburbsweb.service.data.entities.level3.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Builder
@Getter
@AllArgsConstructor
public class CO22Meta {
    @Id
    private Long id;
    @Setter
    private Long t1id;
    private Date requestDate;
    private Long l2PrigIdnum;
    private Long l2PassIdnum;
    private Date operationDate;
    private Date ticketBegDate;
    private Date ticketEndDate;

    public CO22Meta() {}
}
