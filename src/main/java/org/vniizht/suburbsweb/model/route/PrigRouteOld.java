package org.vniizht.suburbsweb.model.route;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class PrigRouteOld {
    Integer type;
    String  stationBeg;
    String  stationEnd;
    String  matterStr;
    Integer matterInt;
    Integer mcd;
    Integer distance;
}
