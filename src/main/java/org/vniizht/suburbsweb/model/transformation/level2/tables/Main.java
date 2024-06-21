package org.vniizht.suburbsweb.model.transformation.level2.tables;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.vniizht.suburbsweb.model.transformation.level2.L2Common;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(schema = "zzz_rawdl2",
        name = "l2_prig_main")
@ToString(callSuper=true)
@Getter
@Setter
public class Main extends L2Common {
    private Long        id;
    private Integer     request_num;
    private String      term_pos;
    private Character   term_dor;
    private String      term_trm;
    private Short       arxiv_code;
    private Short       reply_code;
    private Short       doc_num;
    private String      ticket_ser;
    private Integer     ticket_num;
    private String      kodbl;
    private Time        request_time;
    private Short       request_type;
    private Short       request_subtype;
    private Character   oper;
    private Character   oper_g;
    private Character   registration_method;
    private Date        operation_date;
    private Date        ticket_begdate;
    private Character   train_category;
    private String      train_num;
    private Short       agent_code;
    private Short       carriage_code;
    private Character   paymenttype;
    private String      els_code;
    private String      sale_station;
    private String      region_code;
    private String      payagent_id;
    private String      web_id;
    private String      departure_station;
    private String      arrival_station;
    private String      intermed_station;
    private Short       departure_zone;
    private Short       arrival_zone;
    private Short       intermed_zone;
    private Character   doc_type;
    private Short       pass_qty;
    private Character   carryon_type;
    private Short       carryon_weight;
    private Character   flg_2wayticket;
    private Character   flg_1wayticket;
    private Character   flg_child;
    private Character   flg_military;
    private Character   flg_benefit;
    private Character   flg_so;
    private Character   flg_nu;
    private Character   flg_tt;
    private Short       seatstick_limit;
    private Character   carriage_class;
    private String      benefitgroup_code;
    private String      benefit_code;
    private Character   flg_bsp;
    private Date        ticket_enddate;
    private Date        return_date;
    private String      benefit_region;
    private Long        total_sum;
    private Long        tariff_sum;
    private Long        department_sum;
    private Long        fee_sum;
    private Long        fee_vat;
    private Long        refundfee_sum;
    private Long        refunddepart_sum;
    private Short       military_code;
    private Short       benefit_percent;
    private String      abonement_type;
    private Character   flg_carryon;
    private Character   flg_fee_onboard;
    private Character   flg_fee_o;
    private Character   flg_fee_v;
    private Character   flg_service;
    private String      user_id;
    private String      payment_id;
    private Character   spectarif_code;
    private Integer     date_template;
    private String      server_datetime;
    private Integer     server_reqnum;
    private Integer     server_stcode;
    private Character   abonement_subtype;
    private Character   flg_official_benefit;
    private Character   no_use;
    private Timestamp   write_time;
}
