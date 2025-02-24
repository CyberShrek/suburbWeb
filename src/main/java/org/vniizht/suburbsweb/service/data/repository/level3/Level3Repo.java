package org.vniizht.suburbsweb.service.data.repository.level3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Date;

@NoRepositoryBean
public interface Level3Repo<ENTITY, ID> extends JpaRepository<ENTITY, ID> {
    ENTITY findByOrderByKeyRequestDateDesc();
    void deleteAllByKeyRequestDate(Date date);
}
