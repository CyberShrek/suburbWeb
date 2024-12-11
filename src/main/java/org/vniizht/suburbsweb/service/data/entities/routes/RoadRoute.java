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
public class RoadRoute extends Route {
    private String    roadStart;
    private String    roadEnd;
    private String    road;
}
