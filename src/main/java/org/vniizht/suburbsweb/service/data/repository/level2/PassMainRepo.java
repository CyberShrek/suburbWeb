package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PassMain;

import java.util.Date;
import java.util.List;

@Repository
public interface PassMainRepo extends Level2Repo<PassMain, Long> {

    @Query(value = "SELECT main.idnum FROM rawdl2.l2_pass_main main " +
            "WHERE main.request_date = ?1 " +
            "AND main.f_r10af3[8] = true", nativeQuery = true)
    List<Long> findIdnumByRequestDate(Date date);

    @Query(value = "SELECT main.* FROM rawdl2.l2_pass_main main " +
            "LEFT JOIN rawdl2.l2_pass_main_upd upd  ON (main.idnum = upd.idnum) " +
            "LEFT JOIN rawdl2.l2_pass_ex ex         ON (main.idnum = ex.idnum AND ex.npp = 1) " +
            "LEFT JOIN rawdl2.l2_pass_refund refund ON (main.idnum = refund.idnum) " +
            "LEFT JOIN rawdl2.l2_pass_cost costs    ON (main.idnum = costs.idnum)" +
            "WHERE (upd.no_use IS NULL OR upd.no_use != '1') " +
            "AND main.request_date = ?1 " +
            "AND main.idnum IN ?2 " +
            "AND main.f_r10af3[8] = true", nativeQuery = true)
    List<PassMain> findAllByRequestDateAndIdnumIn(Date date, List<Long> ids);
}