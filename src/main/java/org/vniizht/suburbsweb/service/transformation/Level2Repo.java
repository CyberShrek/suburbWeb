package org.vniizht.suburbsweb.service.transformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Adi;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;

import java.util.List;

@Repository
interface Level2Repo<ENTITY, PK> extends JpaRepository<ENTITY, PK> {
    List<ENTITY> findAllByIdnumGreaterThan(Long id);
}

@Repository
interface AdiRepo extends Level2Repo<Adi, Long> {

}

@Repository
interface CostRepo extends Level2Repo<Cost, Cost.Identifier> {

}

@Repository
interface MainRepo extends Level2Repo<Main, Long> {

}

