package org.vniizht.suburbsweb.service.data.entities.routes;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
public class DcsRoute extends RoadRoute {
    private String dcs;
}
