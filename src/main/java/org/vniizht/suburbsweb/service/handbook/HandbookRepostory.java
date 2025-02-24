package org.vniizht.suburbsweb.service.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.service.data.entities.handbook.*;

import java.util.Date;
import java.util.List;

@NoRepositoryBean
interface ReferenceRepository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {

}

@Repository
interface DorRepository extends ReferenceRepository<Dor, Integer> {
    Dor findFirstByKod(Character kod);
}

@Repository
interface StanvRepository extends ReferenceRepository<Stanv, Integer> {
    List<Stanv> findAllByOrderByDatandDesc();
}

@Repository
interface SiteRepository extends ReferenceRepository<Site, Integer> {
    List<Site> findAllByOrderByDatanDesc();
}

@Repository
interface PlagnRepository extends ReferenceRepository<Plagn, Integer> {
    List<Plagn> findAllByOrderByDatanDesc();
}

@Repository
interface SfRepository extends ReferenceRepository<Sf, Integer> {
    List<Sf> findAllByOrderByDatanDesc();
}

@Repository
interface SublxRepository extends ReferenceRepository<Sublx, Integer> {
    List<Sublx> findAllByOrderByDatanDesc();
}

@Repository
interface TripsRepository extends JpaRepository<SeasonTrip, Integer> {
    List<SeasonTrip> findAllByOrderByDateStartDesc();
}

