package org.vniizht.suburbsweb.service.data.entities.level3.co22;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.vniizht.suburbsweb.service.data.entities.routes.FollowRoute;
import org.vniizht.suburbsweb.util.Util;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@ToString
public class T4 extends T2T6Abstract {
    private String          p5;
    private String          p6;
    private Float           p7;
    private Float           p8;
    private Short           p9;

    public T4(Date requestDate, FollowRoute route,
              double regionIncomePerKm,
              double regionOutcomePerKm) {
        super("tab4", requestDate, route);
        p5 = Util.addLeadingZeros(route.getRoad(), 3);
        p6 = route.getOkato();
        p7 = (float) (route.getDistance() * regionIncomePerKm);
        p8 = (float) (route.getDistance() * regionOutcomePerKm);
        p9 = route.getDistance();
    }
}
