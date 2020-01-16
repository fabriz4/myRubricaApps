package com.rubrica.app.repository;
import com.rubrica.app.domain.Contatti;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Spring Data  repository for the Contatti entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContattiRepository extends JpaRepository<Contatti, Long> {
//Mostra solo i contatti che appartengono all'utente loggato
    @Query("select contatti from Contatti contatti where contatti.owner =:owner")
    Page<Contatti> findByUser(Pageable pageable, @Param("owner") Optional<String> owner);

}
