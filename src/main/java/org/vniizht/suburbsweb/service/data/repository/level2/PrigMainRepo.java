package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;

import java.util.Date;
import java.util.List;

@Repository
public interface PrigMainRepo extends Level2Repo<PrigMain, Long> {

    @Query("SELECT main.idnum FROM PrigMain main WHERE main.requestDate = ?1")
    List<Long> findIdnumByRequestDate(Date date);

    @Query("SELECT main FROM PrigMain main " +
            "LEFT JOIN FETCH main.adi adi " +
            "LEFT JOIN FETCH main.costs costs " +
            "WHERE main.requestDate = ?1 AND main.idnum IN ?2 " +
            "ORDER BY costs.doc_reg")
    List<PrigMain> findAllByRequestDateAndIdnumIn(Date date, List<Long> ids);

//    @Query("SELECT main FROM PrigMain main " +
//            "LEFT OUTER JOIN FETCH main.adi adi " +
//            "LEFT OUTER JOIN FETCH main.costs costs " +
//            "WHERE main.ticket_begdate >= '2024-04-01' " +
//            "AND   main.ticket_enddate >= '2025-04-01' " +
//            "ORDER BY costs.doc_reg"
//    )
//    List<PrigMain> findAllBy2024();
}