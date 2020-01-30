package com.rubrica.app.web.rest;

import com.rubrica.app.domain.Contatti;
import com.rubrica.app.repository.ContattiRepository;
import com.rubrica.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.rubrica.app.security.SecurityUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.rubrica.app.domain.Contatti}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContattiResource {

    private final Logger log = LoggerFactory.getLogger(ContattiResource.class);

    private static final String ENTITY_NAME = "serviziorubricaContatti";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContattiRepository contattiRepository;

    public ContattiResource(final ContattiRepository contattiRepository) {
        this.contattiRepository = contattiRepository;
    }

    /**
     * {@code POST  /contattis} : Create a new contatti.
     *
     * @param contatti the contatti to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new contatti, or with status {@code 400 (Bad Request)} if
     *         the contatti has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contattis")
    public ResponseEntity<Contatti> createContatti(@RequestBody final Contatti contatti) throws URISyntaxException {
        log.debug("REST request to save Contatti : {}", contatti);
        if (contatti.getId() != null) {
            throw new BadRequestAlertException("A new contatti cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (SecurityUtils.getCurrentUserLogin() == null) {
            throw new BadRequestAlertException("Something goes wrong with authentication",ENTITY_NAME,"authfailed");
        }
        //il proprietario del contatto ,che viene inserito, è l'utente corrente.
        //questo campo non può essere modificato o inserito dall'utente.
        contatti.setOwner(SecurityUtils.getCurrentUserLogin().get());
        Contatti result = contattiRepository.save(contatti);
        return ResponseEntity
                .created(new URI("/api/contattis/" + result.getId())).headers(HeaderUtil
                .createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /contattis} : Updates an existing contatti.
     *
     * @param contatti the contatti to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated contatti, or with status {@code 400 (Bad Request)} if the
     *         contatti is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the contatti couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contattis")
    public ResponseEntity<Contatti> updateContatti(@RequestBody final Contatti contatti) throws URISyntaxException {
        log.debug("REST request to update Contatti : {}", contatti);
        if (contatti.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        final Contatti result = contattiRepository.save(contatti);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contatti.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /contattis} : get all the contattis.
     *
     *
     * @param pageable the pagination information.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of contattis in body.
     */
    @GetMapping("/contattis")
    public ResponseEntity<List<Contatti>> getAllContattis(final Pageable pageable) {
        log.debug("REST request to get a page of Contattis");
        final Page<Contatti> page = contattiRepository.findByUser(pageable, SecurityUtils.getCurrentUserLogin());
        final HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contattis/:id} : get the "id" contatti.
     *
     * @param id the id of the contatti to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the contatti, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contattis/{id}")
    public ResponseEntity<Contatti> getContatti(@PathVariable final Long id) {
        log.debug("REST request to get Contatti : {}", id);
        final Optional<Contatti> contatti = contattiRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contatti);
    }

    /**
     * {@code DELETE  /contattis/:id} : delete the "id" contatti.
     *
     * @param id the id of the contatti to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contattis/{id}")
    public ResponseEntity<Void> deleteContatti(@PathVariable final Long id) {
        log.debug("REST request to delete Contatti : {}", id);
        contattiRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
