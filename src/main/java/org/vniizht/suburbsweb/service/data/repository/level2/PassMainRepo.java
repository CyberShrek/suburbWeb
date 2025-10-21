package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PassMain;

import java.util.Date;
import java.util.List;

@Repository
public interface PassMainRepo extends Level2Repo<PassMain, Long> {

//    @Query(value = "SELECT main.idnum FROM zzz_rawdl2.l2_pass_main main " +
//            "WHERE main.request_date = ?1 " +
//            "AND main.f_r10af3[8] = true", nativeQuery = true)
    @Query(value = "SELECT main.idnum FROM rawdl2.l2_pass_main main " +
            "WHERE main.request_date = ?1 " +
            "AND main.f_r10af3[8] = true", nativeQuery = true)
    List<Long> findIdnumByRequestDate(Date date);

//    @Query(value = "SELECT main.* FROM zzz_rawdl2.l2_pass_main main " +
//            "LEFT JOIN zzz_rawdl2.l2_pass_main_upd upd  USING (idnum) " +
//            "LEFT JOIN zzz_rawdl2.l2_pass_ex ex         USING (idnum) " +
//            "LEFT JOIN zzz_rawdl2.l2_pass_refund refund USING (idnum) " +
//            "LEFT JOIN zzz_rawdl2.l2_pass_cost costs    ON costs.idnum = main.id " +
//            "WHERE (upd.no_use IS NULL OR upd.no_use != '1') " +
//            "AND main.request_date = ?1 AND main.idnum IN ?2 " +
//            "AND main.f_r10af3[8] = true", nativeQuery = true)
    @Query(value = "SELECT pm.* FROM rawdl2.l2_pass_main pm " +
            "LEFT JOIN rawdl2.l2_pass_main_upd upd  USING (idnum) " +
            "LEFT JOIN rawdl2.l2_pass_ex ex         USING (idnum) " +
            "LEFT JOIN rawdl2.l2_pass_refund refund USING (idnum) " +
            "LEFT JOIN rawdl2.l2_pass_cost costs    ON pm.id    = costs.idnum " +
            "WHERE (upd.no_use IS NULL OR upd.no_use != '1') " +
            "AND pm.request_date = ?1 AND pm.idnum IN ?2 " +
            "AND pm.f_r10af3[8] = true", nativeQuery = true)
    List<PassMain> findAllByRequestDateAndIdnumIn(Date date, List<Long> ids);
}
