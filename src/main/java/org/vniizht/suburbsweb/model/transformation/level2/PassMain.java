package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "zzz_rawdl2",
        name = "l2_pass_main")
@ToString(callSuper=true)
@Getter
@Setter
public class PassMain extends L2Common {
    private Long        id;
    private Short       request_subtype;
    private Character   oper;
    private Character   oper_g;
//    private Date        operation_date; request_date
//    private Date        ticket_begdate; departure_date
//    private Date        ticket_enddate; не нужен
//    private Character   train_category; по умолчанию 2, для сервисных услуг 3. Сервисные услуги берутся из cost
    private String      train_num;
    private Short       agent_code;
//    private Short       carriage_code; carrier_code?
    private Character   paymenttype;
    private String      sale_station;
//    private String      payagent_id; брать из l2_pass_main_upd
//    private String      web_id;      брать из l2_pass_main_upd
    private String      departure_station;
    private String      arrival_station;
//    private Short       pass_qty; // persons_qty
//    private String      abonement_type; не нужен
//    private Character   carryon_type  ; не нужен
//    private Short       carryon_weight; не нужен
//    private Character   flg_2wayticket; не нужен
//    private Character   flg_child;    f_tick[2]
//    private Character   flg_bsp;      в cost нет записей с sum_code == 116
//    private Character   flg_carryon;  не нужен
//    private Character   flg_fee_onboard; не нужен
//    private Short       seatstick_limit; не нужен
    private Character   carriage_class;
////    private String      benefitgroup_code; первые 2 знака lgot_info из l2_pass_ex
    //    private String      benefit_code; вторые 2 знака lgot_info из l2_pass_ex
    // Потом один знак после минуса -- категория пассажира, потом третий знак после следующего минуса - номер билетной группы

//    private Long        tariff_sum;       sum_nde из cost при payment_type == 1, 6, 8
//    private Long        department_sum;   sum_nde из cost при payment_type != 1, 6, 8
}

// TODO Присоединить l2_pass_ex по id_num
