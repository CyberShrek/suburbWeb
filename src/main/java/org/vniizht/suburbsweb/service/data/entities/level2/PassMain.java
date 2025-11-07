package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_main")
@ToString(callSuper=true)
public class PassMain extends L2Key {
    public Long        id;
    public Short       request_subtype;
    public Time        request_time;
    public Character   oper;
    public Character   oper_g;
    public Date        oper_date;
    public Date        departure_date;
    public Date        arrival_date;
    public String      train_num;
    public Character   train_thread;
    public Short       agent_code;
    public Short       subagent_code;
    public Short       carrier_code;
    public String      saleregion_code;
    public Character   paymenttype;
    public String      sale_station;
    public String      departure_station;
    public String      arrival_station;

    @Column
    public String      f_tick;

    public Character   carriage_class;
    public String      benefit_code;
    public String      benefitcnt_code;
    public Short       military_code;
    public Character   trip_direction;

    public Short       distance;
    public Short       persons_qty;
    public Short       seats_qty;

    @OneToOne(mappedBy = "main")
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassMainUpd upd;

    @OneToMany(mappedBy = "main")
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private List<PassCost> costs;

    @OneToOne(mappedBy = "main")
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassEx  ex;

    @OneToOne(mappedBy = "main")
    @Fetch(FetchMode.JOIN)
    @Getter
    @Setter
    private PassRefund refund;
}