package org.vniizht.suburbsweb.service.data.entities.level3.co22;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.vniizht.suburbsweb.service.data.entities.routes.DcsRoute;
import org.vniizht.suburbsweb.util.Util;

import java.util.Date;

@Getter
@Setter
@SuperBuilder(toBuilder=true)
@ToString
public class T6 extends T2T6Abstract {
    private String          p5;
    private Integer         p6;
    private Short           p7;

    public T6(Date requestDate, DcsRoute route) {
        super("tab6", requestDate, route);
        p5 = Util.addLeadingZeros(route.getRoad(), 3);
        p6 = Integer.valueOf(route.getDcs());
        p7 = route.getDistance();
    }
}
