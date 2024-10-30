package org.vniizht.suburbsweb.service.data.entities.level3;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;


@Getter
@Setter
@ToString
@SuperBuilder(toBuilder=true)
@NoArgsConstructor
public class L3Key {
    private Date   request_date;
    private Integer yyyymm;
}
