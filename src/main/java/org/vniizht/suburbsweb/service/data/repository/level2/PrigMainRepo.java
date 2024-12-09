package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigMain;

import java.util.Date;
import java.util.List;

@Repository
public interface PrigMainRepo extends Level2Repo<PrigMain, Long> {

    @Query("SELECT main FROM PrigMain main " +
            "LEFT OUTER JOIN FETCH main.adi adi " +
            "LEFT OUTER JOIN FETCH main.costs costs " +
            "WHERE main.requestDate = ?1" +
            "ORDER BY costs.doc_reg")
    List<PrigMain> findAllByRequestDate(Date date);
}