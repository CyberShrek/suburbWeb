package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.nsi.*;

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
    Site findFirstByIdsiteAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idsite, String gos, Date datan, Date datak);
}

@Repository
interface PlagnRepository extends NsiRepository<Plagn, Integer> {
    Plagn findFirstByIdplagnAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(String idplagn, String gos, Date datan, Date datak);
}

@Repository
interface SfRepository extends NsiRepository<Sf, Integer> {
    Sf findFirstByVidAndDatanLessThanEqualAndDatakGreaterThanEqual(Integer vid, Date datan, Date datak);
}

@Repository
interface LgotsRepository extends NsiRepository<Lgots, Integer> {
    Lgots findFirstByLgotgrAndLgotAndDatandLessThanEqualAndDatakdGreaterThanEqual(String lgotgr, String lgot, Date datand, Date datakd);
}