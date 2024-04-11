package org.vniizht.suburbsweb.model.transformation.level3.tables_svod;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "svod_work")
@Getter
@Setter
public class Work {
    private short           rez;
    private long            id_svod;
    private short           yyyy;
    private short           mm;
    private short           oper_cnt;
    private Date            request_date;
    private short           agent_code;
    private short           subagent_code;
    private String          term_pos;
    private char            term_dor;
    private short           otd;
    private String          term_trm;
    private int             sale_station;
    private char            flg_oper;
    private short           carrier_cnt;
    private short           carrier_code;
    private char            registration_method;
    private char            flg_soob;
    private char            flg_sng;
    private char            flg_mg;
    private char            flg_prig;
    private short           military_code;
    private char            paymenttype;
    private short           benefitcnt_code;
    private char            sale_channel;
    private char            oper_channel;
    private char            flg_pakr;
    private char            flg_checktape;
    private String          payment_code;
    private char            tick_group;
    private short           doc_qty;
    private short           doc_vz;
    private char            flg_tt;
    private String          els_code;
    private int             kodagnels;
    private String          koddorels;
    private char            prdogels;
    private char            flg_elssubag;
    private String          terminal_posruc;
    private Date            rate_date;
    private long            idnum;
    private short           sum_code;
    private short           cnt_code;
    private short           dor_code;
    private float           sum_nde;
    private float           vat_sum;
    private float           vatrate;
    private String          shipment_type;
    private char            flg_internet;
    private float           card_cost;
    private float           vat_cost;
    private float           sum_sbv;
    private float           vatrate_vz;
    private float           addrat_cost;
    private float           addrat_vat;
}
