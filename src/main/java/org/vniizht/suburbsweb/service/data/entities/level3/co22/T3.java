package org.vniizht.suburbsweb.service.data.entities.level3.co22;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.vniizht.suburbsweb.service.data.entities.routes.RegionRoute;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@ToString
public class T3 extends T2T6Abstract {
    protected String          p5;
    protected String          p6;
    protected Short           p7;

    public T3(Date requestDate, RegionRoute route) {
        super("tab3", requestDate, route);
        p5 = route.getRegion();
        p6 = route.getOkato();
        p7 = route.getDistance();
    }
}
