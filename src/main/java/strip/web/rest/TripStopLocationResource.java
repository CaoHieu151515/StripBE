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
import strip.repository.TripStopLocationRepository;
import strip.service.TripStopLocationService;
import strip.service.dto.TripStopLocationDTO;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.TripStopLocation}.
 */
@RestController
@RequestMapping("/api/trip-stop-locations")
public class TripStopLocationResource {

    private final Logger log = LoggerFactory.getLogger(TripStopLocationResource.class);

    private static final String ENTITY_NAME = "tripStopLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripStopLocationService tripStopLocationService;

    private final TripStopLocationRepository tripStopLocationRepository;

    public TripStopLocationResource(
        TripStopLocationService tripStopLocationService,
        TripStopLocationRepository tripStopLocationRepository
    ) {
        this.tripStopLocationService = tripStopLocationService;
        this.tripStopLocationRepository = tripStopLocationRepository;
    }

    /**
     * {@code POST  /trip-stop-locations} : Create a new tripStopLocation.
     *
     * @param tripStopLocationDTO the tripStopLocationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripStopLocationDTO, or with status {@code 400 (Bad Request)} if the tripStopLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<TripStopLocationDTO> createTripStopLocation(@RequestBody TripStopLocationDTO tripStopLocationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TripStopLocation : {}", tripStopLocationDTO);
        if (tripStopLocationDTO.getId() != null) {
            throw new BadRequestAlertException("A new tripStopLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tripStopLocationDTO = tripStopLocationService.save(tripStopLocationDTO);
        return ResponseEntity.created(new URI("/api/trip-stop-locations/" + tripStopLocationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tripStopLocationDTO.getId().toString()))
            .body(tripStopLocationDTO);
    }

    /**
     * {@code PUT  /trip-stop-locations/:id} : Updates an existing tripStopLocation.
     *
     * @param id the id of the tripStopLocationDTO to save.
     * @param tripStopLocationDTO the tripStopLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripStopLocationDTO,
     * or with status {@code 400 (Bad Request)} if the tripStopLocationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripStopLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<TripStopLocationDTO> updateTripStopLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripStopLocationDTO tripStopLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TripStopLocation : {}, {}", id, tripStopLocationDTO);
        if (tripStopLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripStopLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripStopLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tripStopLocationDTO = tripStopLocationService.update(tripStopLocationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripStopLocationDTO.getId().toString()))
            .body(tripStopLocationDTO);
    }

    /**
     * {@code PATCH  /trip-stop-locations/:id} : Partial updates given fields of an existing tripStopLocation, field will ignore if it is null
     *
     * @param id the id of the tripStopLocationDTO to save.
     * @param tripStopLocationDTO the tripStopLocationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripStopLocationDTO,
     * or with status {@code 400 (Bad Request)} if the tripStopLocationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tripStopLocationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripStopLocationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripStopLocationDTO> partialUpdateTripStopLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripStopLocationDTO tripStopLocationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TripStopLocation partially : {}, {}", id, tripStopLocationDTO);
        if (tripStopLocationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripStopLocationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripStopLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripStopLocationDTO> result = tripStopLocationService.partialUpdate(tripStopLocationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tripStopLocationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-stop-locations} : get all the tripStopLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripStopLocations in body.
     */
    @Hidden
    @GetMapping("")
    public ResponseEntity<List<TripStopLocationDTO>> getAllTripStopLocations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TripStopLocations");
        Page<TripStopLocationDTO> page = tripStopLocationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trip-stop-locations/:id} : get the "id" tripStopLocation.
     *
     * @param id the id of the tripStopLocationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripStopLocationDTO, or with status {@code 404 (Not Found)}.
     */
    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<TripStopLocationDTO> getTripStopLocation(@PathVariable("id") Long id) {
        log.debug("REST request to get TripStopLocation : {}", id);
        Optional<TripStopLocationDTO> tripStopLocationDTO = tripStopLocationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tripStopLocationDTO);
    }

    /**
     * {@code DELETE  /trip-stop-locations/:id} : delete the "id" tripStopLocation.
     *
     * @param id the id of the tripStopLocationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTripStopLocation(@PathVariable("id") Long id) {
        log.debug("REST request to delete TripStopLocation : {}", id);
        tripStopLocationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
