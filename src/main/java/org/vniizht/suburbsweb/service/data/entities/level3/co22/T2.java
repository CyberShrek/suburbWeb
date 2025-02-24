package org.vniizht.suburbsweb.service.data.entities.level3.co22;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.vniizht.suburbsweb.service.data.entities.routes.DepartmentRoute;
import org.vniizht.suburbsweb.util.Util;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@ToString
public class T2 extends T2T6Abstract {
    protected String          p5;
    protected String          p6;
    protected Short           p7;

    public T2(Date requestDate, DepartmentRoute route) {
        super("tab2", requestDate, route);
        p5 = Util.addLeadingZeros(route.getRoad(), 3);
        p6 = route.getDepartment();
        p7 = route.getDistance();
    }
}
