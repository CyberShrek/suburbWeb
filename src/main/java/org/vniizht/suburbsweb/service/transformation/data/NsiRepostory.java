package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.nsi.Dor;
import org.vniizht.suburbsweb.model.transformation.nsi.Plagn;
import org.vniizht.suburbsweb.model.transformation.nsi.Site;
import org.vniizht.suburbsweb.model.transformation.nsi.Stanv;

import java.util.Date;

@NoRepositoryBean
interface NsiRepository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {

}

@Repository
interface DorRepository extends NsiRepository<Dor, Integer> {
    Dor findFirstByKoddAndKodg(Character kodd, String kodg);
}

@Repository
interface StanvRepository extends NsiRepository<Stanv, Integer> {
    Stanv findFirstByStanAndDataniLessThanEqualAndDatakdGreaterThanEqual(String stan, Date date1, Date date2);
}

@Repository
interface SiteRepository extends NsiRepository<Site, Integer> {
    Site getFirstByIdsiteAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idsite, String gos, Date datan, Date datak);
}

@Repository
interface PlagnRepository extends NsiRepository<Plagn, Integer> {
    Plagn getFirstByIdplagnAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idplagn, String gos, Date datan, Date datak);
}