package org.vniizht.suburbsweb.service.data.entities.level3.co22;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.vniizht.suburbsweb.service.data.entities.level3.L3Key;
import org.vniizht.suburbsweb.service.data.entities.routes.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
//@Table(schema = "prigl3",
//        name = "co22_t1")
@Getter
@Setter
@Builder(toBuilder=true)
@ToString
@AllArgsConstructor
public class T1 {

    @EmbeddedId
    private Key key;
    private Long p33;
    private Long p34;
    private Long p35;
    private Long p36;
    private Long p37;
    private Long p38;
    private Long p39;
    private Long p40;
    private Long p41;
    private Long p42;
    private Long p43;
    private Long p44;
    private Long p45;
    private Long p46;
    private Long p47;
    private Long p48;
    private Long p49;
    private Long p50;
    private Long p51;

    public T1() {

    }

    public void setRoutes(RouteGroup routeGroup){

        key.setP13(routeGroup.getFirstRoadRoute() != null ? routeGroup.getFirstRoadRoute().getRoad() : null);
        key.setP14(routeGroup.getFirstDepartmentRoute() != null ? routeGroup.getFirstDepartmentRoute().getDepartment() : null);
        key.setP16(routeGroup.getFirstRegionRoute() != null ? routeGroup.getFirstRegionRoute().getRegion() : null);
        key.setP27(routeGroup.getLastRoadRoute() != null ? routeGroup.getLastRoadRoute().getRoad() : null);
        key.setP28(routeGroup.getLastDepartmentRoute() != null ? routeGroup.getLastDepartmentRoute().getDepartment() : null);
        key.setP29(routeGroup.getLastRegionRoute() != null ? routeGroup.getLastRegionRoute().getRegion() : null);
        key.setP62((short) routeGroup.getMcdRoutes().stream().mapToInt(McdRoute::getDistance).sum());

        if (routeGroup.getMcdRoutes().stream().anyMatch(mcdRoute -> mcdRoute.getCode() == '1')) {
            switch (routeGroup.getMcdRoutes().size()) {
                case 1:  key.setP63('1');
                    break;
                case 2:  key.setP63(routeGroup.getMcdRoutes().get(0).getCode() == 0 ? '2' : '3');
                    break;
                default: key.setP63('4');
                    break;
            }
        } else {
            key.setP63('0');
        }
    }



    @Embeddable
    @SuperBuilder(toBuilder=true)
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    static public class Key extends L3Key implements Serializable {
        private String          p1;
        private Long            p2;
        private String          p3;
        private String          p4;
        private String          p5;
        private String          p6;
        private String          p7;
        private String          p8;
        private String          p9;
        private String          p10;
        private String          p11;
        private String          p12;
        private String          p13;
        private String          p14;
        private String          p15;
        private String          p16;
        private String          p17;
        private String          p18;
        private Character       p19;
        private String          p20;
        private Character       p21;
        private Character       p22;
        private Character       p23;
        private String          p24;
        private Character       p25;
        private String          p26;
        private String          p27;
        private String          p28;
        private String          p29;
        private String          p30;
        private String          p31;
        private Short           p32;
        // ...
        private Character       p52;
        private String          p53;
        private String          p54;
        private Character       p55;
        private String          p56;
        private Character       p57;
        private Character       p58;
        private Character       p59;
        private String          p60;
        private Character       p61;
        private Short           p62;
        private Character       p63;
    }

    public void merge(T1 t1) {
        p33 += t1.p33;
        p34 += t1.p34;
        p35 += t1.p35;
        p36 += t1.p36;
        p37 += t1.p37;
        p38 += t1.p38;
        p39 += t1.p39;
        p40 += t1.p40;
        p41 += t1.p41;
        p42 += t1.p42;
        p43 += t1.p43;
        p44 += t1.p44;
        p45 += t1.p45;
        p46 += t1.p46;
        p47 += t1.p47;
        p48 += t1.p48;
        p49 += t1.p49;
        p50 += t1.p50;
        p51 += t1.p51;
    }
}
