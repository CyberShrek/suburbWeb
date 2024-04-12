package org.vniizht.suburbsweb.model.transformation.level3.tables;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "prig_bil", indexes = @Index(name = "l3_prig_prig_bil_ind", columnList = "nom_bil"))
@Getter
@Setter
public class Bil {
    private int             nom_bil;
    private short           k_pas;
    private short           srok_bil;
    private short           srok_mon;
    private char            oper;
    private char            oper_g;
    private char            flg_bag;
    private char            flg_tuda_obr;
    private char            flg_rab_day;
    private char            flg_ruch;
    private short           request_type;
    private short           request_subtype;
    private String          web_id;
    private char            vid_rasch;
    private char            flg_child;
    private short           flg_voin;
    private char            flg_military;
    private char            flg_lgt;
    private char            flg_bsp;
    private char            flg_so;
    private char            flg_nu;
    private char            flg_tt;
    private String          klass;
    private short           kod_lgt;
    private short           lgt_reg;
    private char            bag_vid;
    private short           bag_ves;
    private short           proc_lgt;
    private char            rzd_fpk;
    private String          abonement_type;
    private char            abonement_subtype;
    private char            flg_official_benefit;
    private char            train_category;
    private char            employee_cat;
    private short           grup_lgt;
    private char            prod;
    private String          flg_sbor;
    private char            flg_bil_sbor;
    private Date            date_zap;
    private int             part_zap;
    private long            idnum;
}
