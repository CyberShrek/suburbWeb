package org.vniizht.suburbsweb.service.data.entities.level3.meta;

import lombok.Builder;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
public class Meta {
    @Id
    private Long id;
    private Long l3id;
    private Long prigIdnum;
    private Long passIdnum;

    public Meta() {}
}
