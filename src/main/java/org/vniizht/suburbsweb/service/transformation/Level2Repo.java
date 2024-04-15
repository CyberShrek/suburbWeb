package org.vniizht.suburbsweb.service.transformation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Adi;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;

@Repository
interface AdiRepo extends JpaRepository<Adi, Long> {

}

@Repository
interface CostRepo extends JpaRepository<Cost, Cost.Identifier> {

}

@Repository
interface MainRepo extends JpaRepository<Main, Long> {

}

