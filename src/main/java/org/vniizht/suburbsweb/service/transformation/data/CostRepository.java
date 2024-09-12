package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.level2.PrigCost;

import java.util.List;

@Repository
public interface CostRepository extends Level2Repository<PrigCost, PrigCost.Identifier> {

    List<PrigCost> findAllByIdnum(Long idnum); // <25>
}
