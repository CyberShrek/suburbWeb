package org.vniizht.suburbsweb.service.data.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Route {
    Short     num;
    Short     serial;

    String    roadStart;
    String    roadEnd;
    String    road;
    String    roadToFollow;
    Short     roadDistance;

    String    okato;

    String    departmentStart;
    String    departmentEnd;
    String    department;
    Short     departmentDistance;

    String    regionStart;
    String    regionEnd;
    String    region;
    Short     regionDistance;

    Short     dcs;
    Short     dcsDistance;

    Character mcd;
    Short     mcdDistance;

    Long      cost;
    Long      lostCost;
    Short     distance;

    public void incrementSerial() {
        serial++;
    }
}
