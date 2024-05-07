package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
import org.vniizht.suburbsweb.model.transformation.nsi.Dor;
import org.vniizht.suburbsweb.model.transformation.nsi.Sf;
import org.vniizht.suburbsweb.model.transformation.nsi.Stanv;

import java.util.Date;

@NoRepositoryBean
interface NsiRepository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {

}

@Repository
interface DorRepository extends NsiRepository<Dor, Integer> {
    Dor findFirstByKoddAndKodg(Character kodd, String kodg);
    Dor findFirstByVcAndKodg(Character vc, String kodg);
}

@Repository
interface SfRepository extends NsiRepository<Sf, Integer> {
}

@Repository
interface StanvRepository extends NsiRepository<Stanv, Integer> {
    Stanv findFirstByStanAndDatandGreaterThanEqualAndDatakdLessThanEqual(String stan, Date date1, Date date2);
}

