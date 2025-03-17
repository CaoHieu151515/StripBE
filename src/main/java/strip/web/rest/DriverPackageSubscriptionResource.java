package strip.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import strip.domain.DriverPackageSubscription;
import strip.repository.DriverPackageSubscriptionRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.DriverPackageSubscription}.
 */
@RestController
@RequestMapping("/api/driver-package-subscriptions")
@Transactional
public class DriverPackageSubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(DriverPackageSubscriptionResource.class);

    private static final String ENTITY_NAME = "driverPackageSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DriverPackageSubscriptionRepository driverPackageSubscriptionRepository;

    public DriverPackageSubscriptionResource(DriverPackageSubscriptionRepository driverPackageSubscriptionRepository) {
        this.driverPackageSubscriptionRepository = driverPackageSubscriptionRepository;
    }

    /**
     * {@code POST  /driver-package-subscriptions} : Create a new driverPackageSubscription.
     *
     * @param driverPackageSubscription the driverPackageSubscription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new driverPackageSubscription, or with status {@code 400 (Bad Request)} if the driverPackageSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DriverPackageSubscription> createDriverPackageSubscription(
        @RequestBody DriverPackageSubscription driverPackageSubscription
    ) throws URISyntaxException {
        log.debug("REST request to save DriverPackageSubscription : {}", driverPackageSubscription);
        if (driverPackageSubscription.getId() != null) {
            throw new BadRequestAlertException("A new driverPackageSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        driverPackageSubscription = driverPackageSubscriptionRepository.save(driverPackageSubscription);
        return ResponseEntity.created(new URI("/api/driver-package-subscriptions/" + driverPackageSubscription.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, driverPackageSubscription.getId().toString()))
            .body(driverPackageSubscription);
    }

    /**
     * {@code PUT  /driver-package-subscriptions/:id} : Updates an existing driverPackageSubscription.
     *
     * @param id the id of the driverPackageSubscription to save.
     * @param driverPackageSubscription the driverPackageSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driverPackageSubscription,
     * or with status {@code 400 (Bad Request)} if the driverPackageSubscription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the driverPackageSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DriverPackageSubscription> updateDriverPackageSubscription(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DriverPackageSubscription driverPackageSubscription
    ) throws URISyntaxException {
        log.debug("REST request to update DriverPackageSubscription : {}, {}", id, driverPackageSubscription);
        if (driverPackageSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverPackageSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driverPackageSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        driverPackageSubscription = driverPackageSubscriptionRepository.save(driverPackageSubscription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, driverPackageSubscription.getId().toString()))
            .body(driverPackageSubscription);
    }

    /**
     * {@code PATCH  /driver-package-subscriptions/:id} : Partial updates given fields of an existing driverPackageSubscription, field will ignore if it is null
     *
     * @param id the id of the driverPackageSubscription to save.
     * @param driverPackageSubscription the driverPackageSubscription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated driverPackageSubscription,
     * or with status {@code 400 (Bad Request)} if the driverPackageSubscription is not valid,
     * or with status {@code 404 (Not Found)} if the driverPackageSubscription is not found,
     * or with status {@code 500 (Internal Server Error)} if the driverPackageSubscription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DriverPackageSubscription> partialUpdateDriverPackageSubscription(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DriverPackageSubscription driverPackageSubscription
    ) throws URISyntaxException {
        log.debug("REST request to partial update DriverPackageSubscription partially : {}, {}", id, driverPackageSubscription);
        if (driverPackageSubscription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, driverPackageSubscription.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!driverPackageSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DriverPackageSubscription> result = driverPackageSubscriptionRepository
            .findById(driverPackageSubscription.getId())
            .map(existingDriverPackageSubscription -> {
                if (driverPackageSubscription.getPurchaseDate() != null) {
                    existingDriverPackageSubscription.setPurchaseDate(driverPackageSubscription.getPurchaseDate());
                }
                if (driverPackageSubscription.getExpirationDate() != null) {
                    existingDriverPackageSubscription.setExpirationDate(driverPackageSubscription.getExpirationDate());
                }
                if (driverPackageSubscription.getPackagePrice() != null) {
                    existingDriverPackageSubscription.setPackagePrice(driverPackageSubscription.getPackagePrice());
                }
                if (driverPackageSubscription.getActive() != null) {
                    existingDriverPackageSubscription.setActive(driverPackageSubscription.getActive());
                }

                return existingDriverPackageSubscription;
            })
            .map(driverPackageSubscriptionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, driverPackageSubscription.getId().toString())
        );
    }

    /**
     * {@code GET  /driver-package-subscriptions} : get all the driverPackageSubscriptions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of driverPackageSubscriptions in body.
     */
    @GetMapping("")
    public List<DriverPackageSubscription> getAllDriverPackageSubscriptions() {
        log.debug("REST request to get all DriverPackageSubscriptions");
        return driverPackageSubscriptionRepository.findAll();
    }

    /**
     * {@code GET  /driver-package-subscriptions/:id} : get the "id" driverPackageSubscription.
     *
     * @param id the id of the driverPackageSubscription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the driverPackageSubscription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DriverPackageSubscription> getDriverPackageSubscription(@PathVariable("id") UUID id) {
        log.debug("REST request to get DriverPackageSubscription : {}", id);
        Optional<DriverPackageSubscription> driverPackageSubscription = driverPackageSubscriptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(driverPackageSubscription);
    }

    /**
     * {@code DELETE  /driver-package-subscriptions/:id} : delete the "id" driverPackageSubscription.
     *
     * @param id the id of the driverPackageSubscription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriverPackageSubscription(@PathVariable("id") UUID id) {
        log.debug("REST request to delete DriverPackageSubscription : {}", id);
        driverPackageSubscriptionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
