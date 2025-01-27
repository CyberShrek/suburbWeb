package org.vniizht.suburbsweb.service.data.entities.level3.co22;

import lombok.Getter;
import lombok.Setter;
import org.vniizht.suburbsweb.service.data.entities.routes.Route;

import java.util.Date;

@Getter
@Setter
public abstract class T2T6Abstract {
    protected Date   requestDate;
    protected String          p1;
    protected String          p2;
    protected Long            p3;
    protected Short           p4;

    public T2T6Abstract(String tableName, Date requestDate, Route route) {
        this.requestDate = requestDate;
        p1 = tableName;
        p2 = "017";
        p4 = route.getSerial();
    }
}
