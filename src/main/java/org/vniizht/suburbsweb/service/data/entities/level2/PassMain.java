package org.vniizht.suburbsweb.service.data.entities.level2;

import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
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
    public byte[]      f_tick;
    @Column
    public byte[]      f_r10af3;
    public Character   carriage_class;
    public String      benefit_code;
    public String      benefitcnt_code;
    public Short       military_code;
    public Character   trip_direction;

    public Short       distance;
    public Short       seats_qty;
}