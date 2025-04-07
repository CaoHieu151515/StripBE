package strip.web.rest;

import io.swagger.v3.oas.annotations.Hidden;
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
import strip.domain.WalletDeposit;
import strip.repository.WalletDepositRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.WalletDeposit}.
 */
@RestController
@RequestMapping("/api/wallet-deposits")
@Transactional
public class WalletDepositResource {

    private static final Logger LOG = LoggerFactory.getLogger(WalletDepositResource.class);

    private static final String ENTITY_NAME = "walletDeposit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletDepositRepository walletDepositRepository;

    public WalletDepositResource(WalletDepositRepository walletDepositRepository) {
        this.walletDepositRepository = walletDepositRepository;
    }

    /**
     * {@code POST  /wallet-deposits} : Create a new walletDeposit.
     *
     * @param walletDeposit the walletDeposit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new walletDeposit, or with status {@code 400 (Bad Request)} if the walletDeposit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<WalletDeposit> createWalletDeposit(@RequestBody WalletDeposit walletDeposit) throws URISyntaxException {
        LOG.debug("REST request to save WalletDeposit : {}", walletDeposit);
        if (walletDeposit.getId() != null) {
            throw new BadRequestAlertException("A new walletDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        walletDeposit = walletDepositRepository.save(walletDeposit);
        return ResponseEntity.created(new URI("/api/wallet-deposits/" + walletDeposit.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, walletDeposit.getId().toString()))
            .body(walletDeposit);
    }

    /**
     * {@code PUT  /wallet-deposits/:id} : Updates an existing walletDeposit.
     *
     * @param id the id of the walletDeposit to save.
     * @param walletDeposit the walletDeposit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletDeposit,
     * or with status {@code 400 (Bad Request)} if the walletDeposit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the walletDeposit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<WalletDeposit> updateWalletDeposit(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody WalletDeposit walletDeposit
    ) throws URISyntaxException {
        LOG.debug("REST request to update WalletDeposit : {}, {}", id, walletDeposit);
        if (walletDeposit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletDeposit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletDepositRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        walletDeposit = walletDepositRepository.save(walletDeposit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletDeposit.getId().toString()))
            .body(walletDeposit);
    }

    /**
     * {@code PATCH  /wallet-deposits/:id} : Partial updates given fields of an existing walletDeposit, field will ignore if it is null
     *
     * @param id the id of the walletDeposit to save.
     * @param walletDeposit the walletDeposit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletDeposit,
     * or with status {@code 400 (Bad Request)} if the walletDeposit is not valid,
     * or with status {@code 404 (Not Found)} if the walletDeposit is not found,
     * or with status {@code 500 (Internal Server Error)} if the walletDeposit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WalletDeposit> partialUpdateWalletDeposit(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody WalletDeposit walletDeposit
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WalletDeposit partially : {}, {}", id, walletDeposit);
        if (walletDeposit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletDeposit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletDepositRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WalletDeposit> result = walletDepositRepository
            .findById(walletDeposit.getId())
            .map(existingWalletDeposit -> {
                if (walletDeposit.getBankNumber() != null) {
                    existingWalletDeposit.setBankNumber(walletDeposit.getBankNumber());
                }
                if (walletDeposit.getNameOfBank() != null) {
                    existingWalletDeposit.setNameOfBank(walletDeposit.getNameOfBank());
                }
                if (walletDeposit.getBank() != null) {
                    existingWalletDeposit.setBank(walletDeposit.getBank());
                }
                if (walletDeposit.getAmount() != null) {
                    existingWalletDeposit.setAmount(walletDeposit.getAmount());
                }
                if (walletDeposit.getDate() != null) {
                    existingWalletDeposit.setDate(walletDeposit.getDate());
                }
                if (walletDeposit.getStatus() != null) {
                    existingWalletDeposit.setStatus(walletDeposit.getStatus());
                }

                return existingWalletDeposit;
            })
            .map(walletDepositRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletDeposit.getId().toString())
        );
    }

    /**
     * {@code GET  /wallet-deposits} : get all the walletDeposits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of walletDeposits in body.
     */
    @Hidden
    @GetMapping("")
    public List<WalletDeposit> getAllWalletDeposits() {
        LOG.debug("REST request to get all WalletDeposits");
        return walletDepositRepository.findAll();
    }

    /**
     * {@code GET  /wallet-deposits/:id} : get the "id" walletDeposit.
     *
     * @param id the id of the walletDeposit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the walletDeposit, or with status {@code 404 (Not Found)}.
     */

    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<WalletDeposit> getWalletDeposit(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get WalletDeposit : {}", id);
        Optional<WalletDeposit> walletDeposit = walletDepositRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(walletDeposit);
    }

    /**
     * {@code DELETE  /wallet-deposits/:id} : delete the "id" walletDeposit.
     *
     * @param id the id of the walletDeposit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWalletDeposit(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete WalletDeposit : {}", id);
        walletDepositRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
