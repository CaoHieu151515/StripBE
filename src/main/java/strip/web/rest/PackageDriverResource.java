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
import strip.repository.PackageDriverRepository;
import strip.service.PackageDriverService;
import strip.service.dto.PackageDriverDTO;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.PackageDriver}.
 */
@RestController
@RequestMapping("/api/package-drivers")
public class PackageDriverResource {

    private final Logger log = LoggerFactory.getLogger(PackageDriverResource.class);

    private static final String ENTITY_NAME = "packageDriver";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageDriverService packageDriverService;

    private final PackageDriverRepository packageDriverRepository;

    public PackageDriverResource(PackageDriverService packageDriverService, PackageDriverRepository packageDriverRepository) {
        this.packageDriverService = packageDriverService;
        this.packageDriverRepository = packageDriverRepository;
    }

    /**
     * {@code POST  /package-drivers} : Create a new packageDriver.
     *
     * @param packageDriverDTO the packageDriverDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageDriverDTO, or with status {@code 400 (Bad Request)} if the packageDriver has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<PackageDriverDTO> createPackageDriver(@RequestBody PackageDriverDTO packageDriverDTO) throws URISyntaxException {
        log.debug("REST request to save PackageDriver : {}", packageDriverDTO);
        if (packageDriverDTO.getId() != null) {
            throw new BadRequestAlertException("A new packageDriver cannot already have an ID", ENTITY_NAME, "idexists");
        }
        packageDriverDTO = packageDriverService.save(packageDriverDTO);
        return ResponseEntity.created(new URI("/api/package-drivers/" + packageDriverDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, packageDriverDTO.getId().toString()))
            .body(packageDriverDTO);
    }

    /**
     * {@code PUT  /package-drivers/:id} : Updates an existing packageDriver.
     *
     * @param id the id of the packageDriverDTO to save.
     * @param packageDriverDTO the packageDriverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageDriverDTO,
     * or with status {@code 400 (Bad Request)} if the packageDriverDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageDriverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<PackageDriverDTO> updatePackageDriver(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PackageDriverDTO packageDriverDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PackageDriver : {}, {}", id, packageDriverDTO);
        if (packageDriverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packageDriverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packageDriverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        packageDriverDTO = packageDriverService.update(packageDriverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packageDriverDTO.getId().toString()))
            .body(packageDriverDTO);
    }

    /**
     * {@code PATCH  /package-drivers/:id} : Partial updates given fields of an existing packageDriver, field will ignore if it is null
     *
     * @param id the id of the packageDriverDTO to save.
     * @param packageDriverDTO the packageDriverDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageDriverDTO,
     * or with status {@code 400 (Bad Request)} if the packageDriverDTO is not valid,
     * or with status {@code 404 (Not Found)} if the packageDriverDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the packageDriverDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PackageDriverDTO> partialUpdatePackageDriver(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PackageDriverDTO packageDriverDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PackageDriver partially : {}, {}", id, packageDriverDTO);
        if (packageDriverDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packageDriverDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packageDriverRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PackageDriverDTO> result = packageDriverService.partialUpdate(packageDriverDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, packageDriverDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /package-drivers} : get all the packageDrivers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageDrivers in body.
     */
    @Hidden
    @GetMapping("")
    public ResponseEntity<List<PackageDriverDTO>> getAllPackageDrivers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of PackageDrivers");
        Page<PackageDriverDTO> page = packageDriverService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /package-drivers/:id} : get the "id" packageDriver.
     *
     * @param id the id of the packageDriverDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageDriverDTO, or with status {@code 404 (Not Found)}.
     */
    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<PackageDriverDTO> getPackageDriver(@PathVariable("id") Long id) {
        log.debug("REST request to get PackageDriver : {}", id);
        Optional<PackageDriverDTO> packageDriverDTO = packageDriverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(packageDriverDTO);
    }

    /**
     * {@code DELETE  /package-drivers/:id} : delete the "id" packageDriver.
     *
     * @param id the id of the packageDriverDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackageDriver(@PathVariable("id") Long id) {
        log.debug("REST request to delete PackageDriver : {}", id);
        packageDriverService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
