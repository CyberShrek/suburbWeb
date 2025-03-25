package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PassMain;

import java.util.Date;
import java.util.List;

@Repository
public interface PassMainRepo extends Level2Repo<PassMain, Long> {

    @Query(value = "SELECT pm.* FROM zzz_rawdl2.l2_pass_main pm " +
            "LEFT JOIN zzz_rawdl2.l2_pass_ex ex      ON pm.idnum = ex.idnum " +
            "LEFT JOIN zzz_rawdl2.l2_pass_cost costs ON pm.id = costs.idnum " +
            "WHERE pm.request_date = ?1 " +
            "AND pm.f_r10af3[8] = true", nativeQuery = true)
//    @Query(value = "SELECT pm.* FROM rawdl2.l2_pass_main pm " +
//            "LEFT JOIN rawdl2.l2_pass_ex ex      ON pm.idnum = ex.idnum " +
//            "LEFT JOIN rawdl2.l2_pass_cost costs ON pm.id = costs.idnum " +
//            "WHERE pm.request_date = ?1 " +
//            "AND pm.f_r10af3[8] = true", nativeQuery = true)

    List<PassMain> findAllByRequestDate(Date date);
}
