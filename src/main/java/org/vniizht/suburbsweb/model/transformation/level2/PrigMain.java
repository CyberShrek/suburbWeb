package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(schema = "zzz_rawdl2",
        name = "l2_prig_main")
@ToString(callSuper=true)
@Getter
@Setter
public class PrigMain extends L2Common {
    private Long        id;
    private Short       request_subtype;
    private Character   oper;
    private Character   oper_g;
    private Date        operation_date;
    private Date        ticket_begdate;
    private Date        ticket_enddate;
    private Character   train_category;
    private String      train_num;
    private Short       agent_code;
    private Short       carriage_code;
    private Character   paymenttype;
    private String      sale_station;
    private String      region_code;
    private String      payagent_id;
    private String      web_id;
    private String      departure_station;
    private String      arrival_station;
    private Short       pass_qty;
    private String      abonement_type;
    private Character   carryon_type  ;
    private Short       carryon_weight;
    private Character   flg_2wayticket;
    private Character   flg_child;
    private Character   flg_bsp;
    private Character   flg_carryon;
    private Character   flg_fee_onboard;
    private Short       seatstick_limit;
    private Character   carriage_class;
    private String      benefit_code;
    private String      benefitgroup_code;
    private Long        tariff_sum;
    private Long        department_sum;
}