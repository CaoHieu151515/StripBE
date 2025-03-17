package strip.web.rest;

import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import strip.repository.RequestTripRepository;
import strip.service.RequestTripService;
import strip.service.dto.RequestTripDTO;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.RequestTrip}.
 */
@RestController
@RequestMapping("/api/request-trips")
public class RequestTripResource {

    private final Logger log = LoggerFactory.getLogger(RequestTripResource.class);

    private static final String ENTITY_NAME = "requestTrip";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestTripService requestTripService;

    private final RequestTripRepository requestTripRepository;

    public RequestTripResource(RequestTripService requestTripService, RequestTripRepository requestTripRepository) {
        this.requestTripService = requestTripService;
        this.requestTripRepository = requestTripRepository;
    }

    /**
     * {@code POST  /request-trips} : Create a new requestTrip.
     *
     * @param requestTripDTO the requestTripDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestTripDTO, or with status {@code 400 (Bad Request)} if the requestTrip has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<RequestTripDTO> createRequestTrip(@RequestBody RequestTripDTO requestTripDTO) throws URISyntaxException {
        log.debug("REST request to save RequestTrip : {}", requestTripDTO);
        if (requestTripDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestTrip cannot already have an ID", ENTITY_NAME, "idexists");
        }
        requestTripDTO = requestTripService.save(requestTripDTO);
        return ResponseEntity.created(new URI("/api/request-trips/" + requestTripDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, requestTripDTO.getId().toString()))
            .body(requestTripDTO);
    }

    /**
     * {@code PUT  /request-trips/:id} : Updates an existing requestTrip.
     *
     * @param id the id of the requestTripDTO to save.
     * @param requestTripDTO the requestTripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTripDTO,
     * or with status {@code 400 (Bad Request)} if the requestTripDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestTripDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<RequestTripDTO> updateRequestTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RequestTripDTO requestTripDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RequestTrip : {}, {}", id, requestTripDTO);
        if (requestTripDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestTripDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestTripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        requestTripDTO = requestTripService.update(requestTripDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestTripDTO.getId().toString()))
            .body(requestTripDTO);
    }

    /**
     * {@code PATCH  /request-trips/:id} : Partial updates given fields of an existing requestTrip, field will ignore if it is null
     *
     * @param id the id of the requestTripDTO to save.
     * @param requestTripDTO the requestTripDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestTripDTO,
     * or with status {@code 400 (Bad Request)} if the requestTripDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestTripDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestTripDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RequestTripDTO> partialUpdateRequestTrip(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RequestTripDTO requestTripDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RequestTrip partially : {}, {}", id, requestTripDTO);
        if (requestTripDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestTripDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestTripRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RequestTripDTO> result = requestTripService.partialUpdate(requestTripDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestTripDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /request-trips} : get all the requestTrips.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestTrips in body.
     */
    @Hidden
    @GetMapping("")
    public ResponseEntity<List<RequestTripDTO>> getAllRequestTrips(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of RequestTrips");
        Page<RequestTripDTO> page = requestTripService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /request-trips/:id} : get the "id" requestTrip.
     *
     * @param id the id of the requestTripDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestTripDTO, or with status {@code 404 (Not Found)}.
     */
    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<RequestTripDTO> getRequestTrip(@PathVariable("id") Long id) {
        log.debug("REST request to get RequestTrip : {}", id);
        Optional<RequestTripDTO> requestTripDTO = requestTripService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestTripDTO);
    }

    /**
     * {@code DELETE  /request-trips/:id} : delete the "id" requestTrip.
     *
     * @param id the id of the requestTripDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestTrip(@PathVariable("id") Long id) {
        log.debug("REST request to delete RequestTrip : {}", id);
        requestTripService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
