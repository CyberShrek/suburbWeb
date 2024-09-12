package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.reference.*;

import java.util.Date;

@NoRepositoryBean
interface ReferenceRepository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {

}

@Repository
interface DorRepository extends ReferenceRepository<Dor, Integer> {
    Dor findFirstByKoddAndKodg(Character kodd, String kodg);
}

@Repository
interface StanvRepository extends ReferenceRepository<Stanv, Integer> {
    Stanv findFirstByStanAndDataniLessThanEqualAndDatakdGreaterThanEqual(String stan, Date datani, Date datakd);
}

@Repository
interface SiteRepository extends ReferenceRepository<Site, Integer> {
    Site findFirstByIdsiteAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idsite, String gos, Date datan, Date datak);
}

@Repository
interface PlagnRepository extends ReferenceRepository<Plagn, Integer> {
    Plagn findFirstByIdplagnAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idplagn, String gos, Date datan, Date datak);
}

@Repository
interface SfRepository extends ReferenceRepository<Sf, Integer> {
    Sf findFirstByVidAndDatanLessThanEqualAndDatakGreaterThanEqual(Integer vid, Date datan, Date datak);
}

@Repository
interface SublxRepository extends ReferenceRepository<Sublx, Integer> {
    Sublx findFirstByLgAndDatanLessThanEqualAndDatakGreaterThanEqual(String lg, Date datan, Date datak);
}

