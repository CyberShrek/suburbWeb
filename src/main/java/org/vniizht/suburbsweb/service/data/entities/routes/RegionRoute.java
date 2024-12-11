package org.vniizht.suburbsweb.service.data.entities.routes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
public class RegionRoute extends Route {
    private String    regionStart;
    private String    regionEnd;
    private String    region;
    private String    okato;
}
