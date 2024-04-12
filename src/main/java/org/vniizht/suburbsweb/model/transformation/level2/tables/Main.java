package org.vniizht.suburbsweb.model.transformation.level2.tables;

import lombok.Getter;
import lombok.Setter;
import org.vniizht.suburbsweb.model.transformation.level2.AbstractParent;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name = "l2_prig_main", indexes = {
        @Index(name = "l2_prig_main_ind0", columnList = "kodbi"),
        @Index(name = "l2_prig_main_indid", columnList = "id, doc_num")
})
@Getter
@Setter
public class Main extends AbstractParent {
    private Time        request_time;
    private short       request_type;
    private short       request_subtype;
    private char        oper;
    private char        oper_g;
    private char        registration_method;
    private Date        operation_date;
    private Date        ticket_begdate;
    private char        train_category;
    private String      train_num;
    private short       agent_code;
    private short       carriage_code;
    private char        paymenttype;
    private String      channels_code;
    private String      sale_station;
    private String      region_code;
    private String      payagent_id;
    private String      web_id;
    private String      departure_station;
    private String      arrival_station;
    private String      intermed_station;
    private short       departure_zone;
    private short       arrival_zone;
    private short       intermed_zone;
    private char        doc_type;
    private short       pass_qty;
    private char        carryon_type;
    private short       carryon_weight;
    private char        flg_2wayticket;
    private char        flg_1wayticket;
    private char        flg_child;
    private char        flg_military;
    private char        flg_benefit;
    private char        flg_so;
    private char        flg_nu;
    private char        flg_tt;
    private short       seatstick_limit;
    private char        carriage_class;
    private String      benefitgroup_code;
    private String      benefit_code;
    private char        flg_bsp;
    private Date        ticket_enddate;
    private Date        return_date;
    private String      benefit_region;
    private long        total_sum;
    private long        tariff_sum;
    private long        department_sum;
    private long        fee_sum;
    private long        fee_vat;
    private long        refundfee_sum;
    private long        refunddepart_sum;
    private short       military_code;
    private short       benefit_percent;
    private String      abonement_type;
    private char        flg_carryon;
    private char        flg_fee_onboard;
    private char        flg_fee_o;
    private char        flg_fee_v;
    private char        flg_service;
    private String      user_id;
    private String      payment_id;
    private char        spectarif_code;
    private int         date_template;
    private String      server_datetime;
    private int         server_reqnum;
    private int         server_stcode;
    private char        abonement_subtype;
    private char        flg_official_benefit;
    private char        no_use;
    private Timestamp   write_time;
}
