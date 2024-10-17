package org.vniizht.suburbsweb.model.misc;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class Route {
    Integer type;
    String  stationBeg;
    String  stationEnd;
    String  value;
    Integer distance;
}
