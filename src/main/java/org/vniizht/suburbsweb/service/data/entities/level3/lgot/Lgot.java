package org.vniizht.suburbsweb.service.data.entities.level3.lgot;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.vniizht.suburbsweb.service.data.entities.level3.L3Key;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(schema = "prigl3",
        name = "lgot")
@SuperBuilder(toBuilder=true)
@ToString
@NoArgsConstructor
public class Lgot {

    @EmbeddedId
    private Key         key;
    private Short       p19;
    private Integer     p27;
    private Integer     p28;
    private Short       p33;

    @Embeddable
    @SuperBuilder(toBuilder=true)
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @ToString
    @NoArgsConstructor
    static public class Key extends L3Key implements Serializable {
        private String      list;
        private Integer     p1;
        private String      p2;
        private String      p3;
        private Character   p4;
        private Character   p5;
        private Character   p6;
        private String      p7;
        private String      p8;
        private String      p9;
        private String      p10;
        private String      p11;
        private String      p12;
        private Character   p13;
        private String      p14;
        private String      p15;
        private Byte        p16;
        private Boolean     p17;
        private Byte        p18;

        private Character   p20;
        private Short       p21;
        private Date        p22;
        private Date        p23;
        private String      p24;
        private String      p25;
        private String      p26;

        private String      p29;
        private String      p30;
        private String      p31;
        private String      p32;
    }
}
