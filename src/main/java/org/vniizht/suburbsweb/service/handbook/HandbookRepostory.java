package org.vniizht.suburbsweb.service.handbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.handbook.*;
import java.util.Date;
import java.util.List;

@NoRepositoryBean
interface ReferenceRepository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {

}

@Repository
interface DorRepository extends ReferenceRepository<Dor, Integer> {
    Dor findFirstByKoddAndKodg(Character kodd, String kodg);
}

@Repository
interface StanvRepository extends ReferenceRepository<Stanv, Integer> {
    List<Stanv> findAllByOrderByDatandDesc();
    Stanv findFirstByStanAndDatandLessThanEqualAndDatakdGreaterThanEqual(String stan, Date datand, Date datakd);
}

@Repository
interface SiteRepository extends ReferenceRepository<Site, Integer> {
    List<Site> findAllByOrderByDatanDesc();
    Site findFirstByIdsiteAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idsite, String gos, Date datan, Date datak);
}

@Repository
interface PlagnRepository extends ReferenceRepository<Plagn, Integer> {
    List<Plagn> findAllByOrderByDatanDesc();
    Plagn findFirstByIdplagnAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idplagn, String gos, Date datan, Date datak);
}

@Repository
interface SfRepository extends ReferenceRepository<Sf, Integer> {
    List<Sf> findAllByOrderByDatanDesc();
    Sf findFirstByVidAndDatanLessThanEqualAndDatakGreaterThanEqual(Integer vid, Date datan, Date datak);
}

@Repository
interface SublxRepository extends ReferenceRepository<Sublx, Integer> {
    List<Sublx> findAllByOrderByDatanDesc();
    Sublx findFirstByLgAndDatanLessThanEqualAndDatakGreaterThanEqual(String lg, Date datan, Date datak);
}

@Repository
interface TripsRepository extends JpaRepository<Trip, Integer> {
    List<Trip> findAllByOrderByDateStartDesc();
}

