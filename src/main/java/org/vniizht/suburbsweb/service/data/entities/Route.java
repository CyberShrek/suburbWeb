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

    String    departmentStart;
    String    departmentEnd;
    Short     departmentDistance;

    String    regionStart;
    String    regionEnd;
    Short     regionDistance;

    Short     dcs;
    Short     dcsDistance;

    Character mcd;
    Short     mcdDistance;
}
