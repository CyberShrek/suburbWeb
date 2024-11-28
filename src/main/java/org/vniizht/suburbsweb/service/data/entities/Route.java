package org.vniizht.suburbsweb.service.data.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Route {
    Short     num;
    String    roadStart;
    String    roadEnd;
    String    departmentStart;
    String    departmentEnd;
    String    regionStart;
    String    regionEnd;
    Short     mcdDistance;
    Character mcdType;
}
