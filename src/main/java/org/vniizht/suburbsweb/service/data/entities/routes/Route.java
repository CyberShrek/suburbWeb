package org.vniizht.suburbsweb.service.data.entities.routes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
public abstract class Route {
    Short num;
    Short serial;
    Short distance;

    @Builder
    Route (Short distance) {
        this.distance = distance;
    }
}
