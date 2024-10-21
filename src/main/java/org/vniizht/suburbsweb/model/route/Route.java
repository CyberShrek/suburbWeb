package org.vniizht.suburbsweb.model.route;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
abstract public class Route {
    String roadStart;
    String roadEnd;
    String departmentStart;
    String departmentEnd;
    String regionStart;
    String regionEnd;
}
