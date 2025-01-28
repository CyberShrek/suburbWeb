package org.vniizht.suburbsweb.service.data.entities.level3;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;


@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@MappedSuperclass
public class L3Key {
    @Column(name = "request_date")
    private Date requestDate;
    private Integer yyyymm;
}
