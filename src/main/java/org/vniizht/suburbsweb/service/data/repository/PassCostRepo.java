package org.vniizht.suburbsweb.service.data.repository;

import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.level2.PassCost;

import java.util.List;

@Repository
public interface PassCostRepo extends Level2Repo<PassCost, PassCost.Identifier> {

    List<PassCost> findAllByIdnum(Long idnum); // <25>
}
