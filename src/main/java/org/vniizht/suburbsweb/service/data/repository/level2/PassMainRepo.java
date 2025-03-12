package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PassMain;

import java.util.Date;
import java.util.List;

@Repository
public interface PassMainRepo extends Level2Repo<PassMain, Long> {

    @Query("SELECT pm FROM PassMain pm " +
            "LEFT OUTER JOIN FETCH pm.ex " +
            "LEFT OUTER JOIN FETCH pm.costs " +
            "WHERE pm.requestDate = ?1 " +
            "AND function('array_get', pm.f_r10af3, 8) = true")

    List<PassMain> findAllByRequestDate(Date date);
}
