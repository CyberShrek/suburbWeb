package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.level2.PassCost;

import java.util.List;

@Repository
public interface PassCostRepository extends Level2Repository<PassCost, PassCost.Identifier> {

    List<PassCost> findAllByIdnum(Long idnum); // <25>
}
