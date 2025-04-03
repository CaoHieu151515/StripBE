package strip.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.domain.DriverPointHistory;
import strip.repository.DriverPointHistoryRepository;
import strip.service.DriverPointHistoryService;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.DriverPointHistory}.
 */
@RestController
@RequestMapping("/api/driver-point-histories")
public class DriverPointHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(DriverPointHistoryResource.class);

    private static final String ENTITY_NAME = "driverPointHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DriverPointHistoryService driverPointHistoryService;

    private final DriverPointHistoryRepository driverPointHistoryRepository;

    public DriverPointHistoryResource(
        DriverPointHistoryService driverPointHistoryService,
        DriverPointHistoryRepository driverPointHistoryRepository
    ) {
        this.driverPointHistoryService = driverPointHistoryService;
        this.driverPointHistoryRepository = driverPointHistoryRepository;
    }

    /**
     * {@code POST  /driver-point-histories} : Create a new driverPointHistory.
     *
     * @param driverPointHistory the driverPointHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new driverPointHistory, or with status {@code 400 (Bad Request)} if the driverPointHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DriverPointHistory> createDriverPointHistory(@RequestBody DriverPointHistory driverPointHistory)
        throws URISyntaxException {
        LOG.debug("REST request to save DriverPointHistory : {}", driverPointHistory);
        if (driverPointHistory.getId() != null) {
            throw new BadRequestAlertException("A new driverPointHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        driverPointHistory = driverPointHistoryService.save(driverPointHistory);
        return ResponseEntity.created(new URI("/api/driver-point-histories/" + driverPointHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, driverPointHistory.getId().toString()))
            .body(driverPointHistory);
    }

    /**
     * {@code PUT  /driver-point-histories/:id} : Updates an existing driverPointHistory.
     *
     * @param id the id of the driverPointHistory to save.
     * @param driverPointHistory the driverPointHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driverPointHistory,
     * or with status {@code 400 (Bad Request)} if the driverPointHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the driverPointHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DriverPointHistory> updateDriverPointHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DriverPointHistory driverPointHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update DriverPointHistory : {}, {}", id, driverPointHistory);
        if (driverPointHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverPointHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driverPointHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        driverPointHistory = driverPointHistoryService.update(driverPointHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, driverPointHistory.getId().toString()))
            .body(driverPointHistory);
    }

    /**
     * {@code PATCH  /driver-point-histories/:id} : Partial updates given fields of an existing driverPointHistory, field will ignore if it is null
     *
     * @param id the id of the driverPointHistory to save.
     * @param driverPointHistory the driverPointHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driverPointHistory,
     * or with status {@code 400 (Bad Request)} if the driverPointHistory is not valid,
     * or with status {@code 404 (Not Found)} if the driverPointHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the driverPointHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DriverPointHistory> partialUpdateDriverPointHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DriverPointHistory driverPointHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DriverPointHistory partially : {}, {}", id, driverPointHistory);
        if (driverPointHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverPointHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driverPointHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DriverPointHistory> result = driverPointHistoryService.partialUpdate(driverPointHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, driverPointHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /driver-point-histories} : get all the driverPointHistories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of driverPointHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DriverPointHistory>> getAllDriverPointHistories(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DriverPointHistories");
        Page<DriverPointHistory> page = driverPointHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /driver-point-histories/:id} : get the "id" driverPointHistory.
     *
     * @param id the id of the driverPointHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the driverPointHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverPointHistory> getDriverPointHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DriverPointHistory : {}", id);
        Optional<DriverPointHistory> driverPointHistory = driverPointHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(driverPointHistory);
    }

    /**
     * {@code DELETE  /driver-point-histories/:id} : delete the "id" driverPointHistory.
     *
     * @param id the id of the driverPointHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriverPointHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DriverPointHistory : {}", id);
        driverPointHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
