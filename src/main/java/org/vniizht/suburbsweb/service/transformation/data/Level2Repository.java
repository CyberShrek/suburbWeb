package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;
import java.util.List;

@NoRepositoryBean
interface Level2Repository<ENTITY, PK> extends JpaRepository<ENTITY, PK> {
    List<ENTITY> findAllByIdnumGreaterThan(Long idnum);
    List<ENTITY> findAllByRequestDate(Date date);
}

