package strip.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import strip.domain.SystemTempWallet;
import strip.repository.SystemTempWalletRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.SystemTempWallet}.
 */
@RestController
@RequestMapping("/api/system-temp-wallets")
@Transactional
public class SystemTempWalletResource {

    private final Logger log = LoggerFactory.getLogger(SystemTempWalletResource.class);

    private static final String ENTITY_NAME = "systemTempWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemTempWalletRepository systemTempWalletRepository;

    public SystemTempWalletResource(SystemTempWalletRepository systemTempWalletRepository) {
        this.systemTempWalletRepository = systemTempWalletRepository;
    }

    /**
     * {@code POST  /system-temp-wallets} : Create a new systemTempWallet.
     *
     * @param systemTempWallet the systemTempWallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemTempWallet, or with status {@code 400 (Bad Request)} if the systemTempWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SystemTempWallet> createSystemTempWallet(@RequestBody SystemTempWallet systemTempWallet)
        throws URISyntaxException {
        log.debug("REST request to save SystemTempWallet : {}", systemTempWallet);
        if (systemTempWallet.getId() != null) {
            throw new BadRequestAlertException("A new systemTempWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemTempWallet = systemTempWalletRepository.save(systemTempWallet);
        return ResponseEntity.created(new URI("/api/system-temp-wallets/" + systemTempWallet.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemTempWallet.getId().toString()))
            .body(systemTempWallet);
    }

    /**
     * {@code PUT  /system-temp-wallets/:id} : Updates an existing systemTempWallet.
     *
     * @param id the id of the systemTempWallet to save.
     * @param systemTempWallet the systemTempWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemTempWallet,
     * or with status {@code 400 (Bad Request)} if the systemTempWallet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemTempWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SystemTempWallet> updateSystemTempWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemTempWallet systemTempWallet
    ) throws URISyntaxException {
        log.debug("REST request to update SystemTempWallet : {}, {}", id, systemTempWallet);
        if (systemTempWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemTempWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemTempWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemTempWallet = systemTempWalletRepository.save(systemTempWallet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemTempWallet.getId().toString()))
            .body(systemTempWallet);
    }

    /**
     * {@code PATCH  /system-temp-wallets/:id} : Partial updates given fields of an existing systemTempWallet, field will ignore if it is null
     *
     * @param id the id of the systemTempWallet to save.
     * @param systemTempWallet the systemTempWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemTempWallet,
     * or with status {@code 400 (Bad Request)} if the systemTempWallet is not valid,
     * or with status {@code 404 (Not Found)} if the systemTempWallet is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemTempWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemTempWallet> partialUpdateSystemTempWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemTempWallet systemTempWallet
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemTempWallet partially : {}, {}", id, systemTempWallet);
        if (systemTempWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemTempWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemTempWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemTempWallet> result = systemTempWalletRepository
            .findById(systemTempWallet.getId())
            .map(existingSystemTempWallet -> {
                if (systemTempWallet.getSystemWalletID() != null) {
                    existingSystemTempWallet.setSystemWalletID(systemTempWallet.getSystemWalletID());
                }
                if (systemTempWallet.getBefore() != null) {
                    existingSystemTempWallet.setBefore(systemTempWallet.getBefore());
                }
                if (systemTempWallet.getAmount() != null) {
                    existingSystemTempWallet.setAmount(systemTempWallet.getAmount());
                }
                if (systemTempWallet.getCurrent() != null) {
                    existingSystemTempWallet.setCurrent(systemTempWallet.getCurrent());
                }
                if (systemTempWallet.getMobifyDate() != null) {
                    existingSystemTempWallet.setMobifyDate(systemTempWallet.getMobifyDate());
                }

                return existingSystemTempWallet;
            })
            .map(systemTempWalletRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemTempWallet.getId().toString())
        );
    }

    /**
     * {@code GET  /system-temp-wallets} : get all the systemTempWallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemTempWallets in body.
     */
    @GetMapping("")
    public List<SystemTempWallet> getAllSystemTempWallets() {
        log.debug("REST request to get all SystemTempWallets");
        return systemTempWalletRepository.findAll();
    }

    /**
     * {@code GET  /system-temp-wallets/:id} : get the "id" systemTempWallet.
     *
     * @param id the id of the systemTempWallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemTempWallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SystemTempWallet> getSystemTempWallet(@PathVariable("id") Long id) {
        log.debug("REST request to get SystemTempWallet : {}", id);
        Optional<SystemTempWallet> systemTempWallet = systemTempWalletRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(systemTempWallet);
    }

    /**
     * {@code DELETE  /system-temp-wallets/:id} : delete the "id" systemTempWallet.
     *
     * @param id the id of the systemTempWallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemTempWallet(@PathVariable("id") Long id) {
        log.debug("REST request to delete SystemTempWallet : {}", id);
        systemTempWalletRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
