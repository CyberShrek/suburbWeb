package org.vniizht.suburbsweb.service.data.entities.routes;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder(toBuilder=true)
public class RegionRoute extends Route {
    private String    region;
    private String    okato;

//    public RegionRoute() {
//        super();
//    }
}
