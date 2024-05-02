package org.vniizht.suburbsweb.service.transformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Adi;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;

import java.util.List;

@NoRepositoryBean
interface Level2Repository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {
    List<ENTITY> findAllByIdnumGreaterThan(Long idnum);
}

@Repository
interface AdiRepository extends Level2Repository<Adi, Long> {

}

@Repository
interface CostRepository extends Level2Repository<Cost, Cost.Identifier> {

}

@Repository
interface MainRepository extends Level2Repository<Main, Long> {

}

