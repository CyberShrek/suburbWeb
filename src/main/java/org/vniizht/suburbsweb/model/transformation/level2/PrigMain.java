package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(schema = "rawdl2",
        name = "l2_prig_main")
@ToString(callSuper=true)
public class PrigMain extends L2Common {
    public Long        id;
    public Short       request_subtype;
    public Character   oper;
    public Character   oper_g;
    public Date        operation_date;
    public Date        ticket_begdate;
    public Date        ticket_enddate;
    public String      ticket_ser;
    public Integer     ticket_num;
    public Character   train_category;
    public String      train_num;
    public Short       agent_code;
    public Short       carriage_code;
    public Character   paymenttype;
    public String      sale_station;
    public String      region_code;
    public String      payagent_id;
    public String      web_id;
    public String      departure_station;
    public String      arrival_station;
    public Short       pass_qty;
    public String      abonement_type;
    public Character   abonement_subtype;
    public Character   carryon_type  ;
    public Short       carryon_weight;
    public Character   flg_2wayticket;
    public Character   flg_child;
    public Character   flg_bsp;
    public Character   flg_carryon;
    public Character   flg_fee_onboard;
    public Short       seatstick_limit;
    public Character   carriage_class;
    public String      benefit_region;
    public String      benefit_code;
    public String      benefitgroup_code;
    public Long        fee_sum;
    public Long        refundfee_sum;
    public Long        tariff_sum;
    public Long        department_sum;

    public String      server_datetime;
    public Integer     server_reqnum;
}