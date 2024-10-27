package org.vniizht.suburbsweb.service.data.repository;

import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PrigCost;

import java.util.List;

@Repository
public interface PrigCostRepo extends Level2Repo<PrigCost, PrigCost.Identifier> {

    List<PrigCost> findAllByIdnum(Long idnum); // <25>
}
