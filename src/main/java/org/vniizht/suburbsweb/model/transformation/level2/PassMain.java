package org.vniizht.suburbsweb.model.transformation.level2;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "rawdl2",
        name = "l2_pass_main")
@ToString(callSuper=true)
@Getter
@Setter
public class PassMain extends L2Common {
    private Long        id;
    private Short       request_subtype;
    private Character   oper;
    private Character   oper_g;
    private Date        request_date;
    private Date        oper_date;
    private Date        departure_date;
    private Date        arrival_date;
    private String      train_num;
    private Short       agent_code;
    private Short       subagent_code;
    private Short       carrier_code;
    private String      saleregion_code;
    private Character   paymenttype;
    private String      sale_station;
    private String      departure_station;
    private String      arrival_station;
    private List<Boolean> f_tick;
    private Character   carriage_class;
    private String      benefit_code;
    private Short       military_code;

    private Short       distance;
    private Short       seats_qty;
}