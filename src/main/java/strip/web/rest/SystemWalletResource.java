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
import strip.domain.SystemWallet;
import strip.repository.SystemWalletRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.SystemWallet}.
 */
@RestController
@RequestMapping("/api/system-wallets")
@Transactional
public class SystemWalletResource {

    private final Logger log = LoggerFactory.getLogger(SystemWalletResource.class);

    private static final String ENTITY_NAME = "systemWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemWalletRepository systemWalletRepository;

    public SystemWalletResource(SystemWalletRepository systemWalletRepository) {
        this.systemWalletRepository = systemWalletRepository;
    }

    /**
     * {@code POST  /system-wallets} : Create a new systemWallet.
     *
     * @param systemWallet the systemWallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemWallet, or with status {@code 400 (Bad Request)} if the systemWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<SystemWallet> createSystemWallet(@RequestBody SystemWallet systemWallet) throws URISyntaxException {
        log.debug("REST request to save SystemWallet : {}", systemWallet);
        if (systemWallet.getId() != null) {
            throw new BadRequestAlertException("A new systemWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        systemWallet = systemWalletRepository.save(systemWallet);
        return ResponseEntity.created(new URI("/api/system-wallets/" + systemWallet.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, systemWallet.getId().toString()))
            .body(systemWallet);
    }

    /**
     * {@code PUT  /system-wallets/:id} : Updates an existing systemWallet.
     *
     * @param id the id of the systemWallet to save.
     * @param systemWallet the systemWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemWallet,
     * or with status {@code 400 (Bad Request)} if the systemWallet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<SystemWallet> updateSystemWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemWallet systemWallet
    ) throws URISyntaxException {
        log.debug("REST request to update SystemWallet : {}, {}", id, systemWallet);
        if (systemWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        systemWallet = systemWalletRepository.save(systemWallet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemWallet.getId().toString()))
            .body(systemWallet);
    }

    /**
     * {@code PATCH  /system-wallets/:id} : Partial updates given fields of an existing systemWallet, field will ignore if it is null
     *
     * @param id the id of the systemWallet to save.
     * @param systemWallet the systemWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemWallet,
     * or with status {@code 400 (Bad Request)} if the systemWallet is not valid,
     * or with status {@code 404 (Not Found)} if the systemWallet is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemWallet> partialUpdateSystemWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SystemWallet systemWallet
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemWallet partially : {}, {}", id, systemWallet);
        if (systemWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemWallet> result = systemWalletRepository
            .findById(systemWallet.getId())
            .map(existingSystemWallet -> {
                if (systemWallet.getSystemWalletID() != null) {
                    existingSystemWallet.setSystemWalletID(systemWallet.getSystemWalletID());
                }
                if (systemWallet.getBefore() != null) {
                    existingSystemWallet.setBefore(systemWallet.getBefore());
                }
                if (systemWallet.getAmount() != null) {
                    existingSystemWallet.setAmount(systemWallet.getAmount());
                }
                if (systemWallet.getCurrent() != null) {
                    existingSystemWallet.setCurrent(systemWallet.getCurrent());
                }
                if (systemWallet.getBlockAmount() != null) {
                    existingSystemWallet.setBlockAmount(systemWallet.getBlockAmount());
                }
                if (systemWallet.getMobifyDate() != null) {
                    existingSystemWallet.setMobifyDate(systemWallet.getMobifyDate());
                }

                return existingSystemWallet;
            })
            .map(systemWalletRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemWallet.getId().toString())
        );
    }

    /**
     * {@code GET  /system-wallets} : get all the systemWallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemWallets in body.
     */
    @Hidden
    @GetMapping("")
    public List<SystemWallet> getAllSystemWallets() {
        log.debug("REST request to get all SystemWallets");
        return systemWalletRepository.findAll();
    }

    /**
     * {@code GET  /system-wallets/:id} : get the "id" systemWallet.
     *
     * @param id the id of the systemWallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemWallet, or with status {@code 404 (Not Found)}.
     */
    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<SystemWallet> getSystemWallet(@PathVariable("id") Long id) {
        log.debug("REST request to get SystemWallet : {}", id);
        Optional<SystemWallet> systemWallet = systemWalletRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(systemWallet);
    }

    /**
     * {@code DELETE  /system-wallets/:id} : delete the "id" systemWallet.
     *
     * @param id the id of the systemWallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSystemWallet(@PathVariable("id") Long id) {
        log.debug("REST request to delete SystemWallet : {}", id);
        systemWalletRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
