package org.vniizht.suburbsweb.model.transformation.level3.co22;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(schema = "l3_prig",
        name = "co22_t1")
@IdClass(T1.Identifier.class)
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
public class T1 {
    @Id private String          p1;
    @Id private Integer         p2;
    @Id private String          p3;
    @Id private String          p4;
    @Id private String          p5;
    @Id private String          p6;
    @Id private String          p7;
    @Id private String          p8;
    @Id private String          p9;
    @Id private String          p10;
    @Id private String          p11;
    @Id private String          p12;
    @Id private String          p13;
    @Id private String          p14;
    @Id private String          p15;
    @Id private String          p16;
    @Id private String          p17;
    @Id private String          p18;
    @Id private Character       p19;
    @Id private String          p20;
    @Id private Character       p21;
    @Id private Character       p22;
    @Id private Character       p23;
    @Id private String          p24;
    @Id private Character       p25;
    @Id private String          p26;
    @Id private String          p27;
    @Id private String          p28;
    @Id private String          p29;
    @Id private String          p30;
    @Id private String          p31;
    @Id private Short           p32;
        private Long            p33;
        private Long            p34;
        private Long            p35;
        private Long            p36;
        private Long            p37;
        private Long            p38;
        private Long            p39;
        private Long            p40;
        private Long            p41;
        private Long            p42;
        private Long            p43;
        private Long            p44;
        private Long            p45;
        private Long            p46;
        private Long            p47;
        private Long            p48;
        private Long            p49;
        private Long            p50;
        private Long            p51;
    @Id private Character       p52;
    @Id private String          p53;
    @Id private String          p54;
    @Id private Character       p55;
    @Id private String          p56;
    @Id private Character       p57;
    @Id private Character       p58;
    @Id private Character       p59;
    @Id private String          p60;
    @Id private Character       p61;
    @Id private Short           p62;
    @Id private Character       p63;
    @Id private String       routes;

    @AllArgsConstructor
    static public class Identifier implements Serializable {
        private String p1;
        private Integer p2;
        private String p3;
        private String p4;
        private String p5;
        private String p6;
        private String p7;
        private String          p8;
        private String          p9;
        private String          p10;
        private String          p11;
        private String          p12;
        private String          p14;
        private String          p15;
        private String      p16;
        private String      p17;
        private String      p18;
        private Character   p19;
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
        private String          routes;
    }
}
