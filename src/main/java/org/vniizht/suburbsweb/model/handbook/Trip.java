package org.vniizht.suburbsweb.model.handbook;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = "prig",
        name = "season_trip")
@Getter
@Setter
@ToString
public class Trip {

    @Id
    private Integer trip_code_id;
    private String gos;
    private Short season_tick_code;
    private Short period;
    private Short kol_trips;
    private Character pr_period;

    @Column(name = "date_ni")
    private Date dateStart;

    @Column(name = "date_ki")
    private Date dateEnd;
}
