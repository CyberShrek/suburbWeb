package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;

import java.util.List;

@Repository
public interface CostRepository extends Level2Repository<Cost, Cost.Identifier> {

    List<Cost> findAllByIdnum(Long idnum); // <25>
}
