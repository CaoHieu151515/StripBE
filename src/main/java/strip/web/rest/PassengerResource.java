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
import strip.repository.PassengerRepository;
import strip.service.PassengerService;
import strip.service.dto.PassengerDTO;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.Passenger}.
 */
@RestController
@RequestMapping("/api/passengers")
public class PassengerResource {

    private final Logger log = LoggerFactory.getLogger(PassengerResource.class);

    private static final String ENTITY_NAME = "passenger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassengerService passengerService;

    private final PassengerRepository passengerRepository;

    public PassengerResource(PassengerService passengerService, PassengerRepository passengerRepository) {
        this.passengerService = passengerService;
        this.passengerRepository = passengerRepository;
    }

    /**
     * {@code POST  /passengers} : Create a new passenger.
     *
     * @param passengerDTO the passengerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passengerDTO, or with status {@code 400 (Bad Request)} if the passenger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO) throws URISyntaxException {
        log.debug("REST request to save Passenger : {}", passengerDTO);
        if (passengerDTO.getId() != null) {
            throw new BadRequestAlertException("A new passenger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        passengerDTO = passengerService.save(passengerDTO);
        return ResponseEntity.created(new URI("/api/passengers/" + passengerDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, passengerDTO.getId().toString()))
            .body(passengerDTO);
    }

    /**
     * {@code PUT  /passengers/:id} : Updates an existing passenger.
     *
     * @param id the id of the passengerDTO to save.
     * @param passengerDTO the passengerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passengerDTO,
     * or with status {@code 400 (Bad Request)} if the passengerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passengerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTO> updatePassenger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PassengerDTO passengerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Passenger : {}, {}", id, passengerDTO);
        if (passengerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passengerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passengerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        passengerDTO = passengerService.update(passengerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passengerDTO.getId().toString()))
            .body(passengerDTO);
    }

    /**
     * {@code PATCH  /passengers/:id} : Partial updates given fields of an existing passenger, field will ignore if it is null
     *
     * @param id the id of the passengerDTO to save.
     * @param passengerDTO the passengerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passengerDTO,
     * or with status {@code 400 (Bad Request)} if the passengerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the passengerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the passengerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PassengerDTO> partialUpdatePassenger(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PassengerDTO passengerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Passenger partially : {}, {}", id, passengerDTO);
        if (passengerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passengerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passengerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PassengerDTO> result = passengerService.partialUpdate(passengerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, passengerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /passengers} : get all the passengers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passengers in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PassengerDTO>> getAllPassengers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Passengers");
        Page<PassengerDTO> page = passengerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /passengers/:id} : get the "id" passenger.
     *
     * @param id the id of the passengerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passengerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> getPassenger(@PathVariable("id") Long id) {
        log.debug("REST request to get Passenger : {}", id);
        Optional<PassengerDTO> passengerDTO = passengerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passengerDTO);
    }

    /**
     * {@code DELETE  /passengers/:id} : delete the "id" passenger.
     *
     * @param id the id of the passengerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable("id") Long id) {
        log.debug("REST request to delete Passenger : {}", id);
        passengerService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
