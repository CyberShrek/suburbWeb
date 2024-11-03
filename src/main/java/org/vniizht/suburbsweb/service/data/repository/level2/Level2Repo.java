package org.vniizht.suburbsweb.service.data.repository.level2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

@NoRepositoryBean
interface Level2Repo<ENTITY, ID> extends JpaRepository<ENTITY, ID> {
    List<ENTITY> findAllByIdnumGreaterThan(Long idnum);
    List<ENTITY> findAllByRequestDate(Date date);
    ENTITY findByOrderByRequestDateAsc();
}

