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
import org.springframework.web.bind.annotation.*;
import strip.domain.WalletTransaction;
import strip.repository.WalletTransactionRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.WalletTransaction}.
 */
@RestController
@RequestMapping("/api/wallet-transactions")
@Transactional
public class WalletTransactionResource {

    private final Logger log = LoggerFactory.getLogger(WalletTransactionResource.class);

    private static final String ENTITY_NAME = "walletTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalletTransactionRepository walletTransactionRepository;

    public WalletTransactionResource(WalletTransactionRepository walletTransactionRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
    }

    /**
     * {@code POST  /wallet-transactions} : Create a new walletTransaction.
     *
     * @param walletTransaction the walletTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new walletTransaction, or with status {@code 400 (Bad Request)} if the walletTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WalletTransaction> createWalletTransaction(@RequestBody WalletTransaction walletTransaction)
        throws URISyntaxException {
        log.debug("REST request to save WalletTransaction : {}", walletTransaction);
        if (walletTransaction.getId() != null) {
            throw new BadRequestAlertException("A new walletTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        walletTransaction = walletTransactionRepository.save(walletTransaction);
        return ResponseEntity.created(new URI("/api/wallet-transactions/" + walletTransaction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, walletTransaction.getId().toString()))
            .body(walletTransaction);
    }

    /**
     * {@code PUT  /wallet-transactions/:id} : Updates an existing walletTransaction.
     *
     * @param id the id of the walletTransaction to save.
     * @param walletTransaction the walletTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletTransaction,
     * or with status {@code 400 (Bad Request)} if the walletTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the walletTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WalletTransaction> updateWalletTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WalletTransaction walletTransaction
    ) throws URISyntaxException {
        log.debug("REST request to update WalletTransaction : {}, {}", id, walletTransaction);
        if (walletTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        walletTransaction = walletTransactionRepository.save(walletTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletTransaction.getId().toString()))
            .body(walletTransaction);
    }

    /**
     * {@code PATCH  /wallet-transactions/:id} : Partial updates given fields of an existing walletTransaction, field will ignore if it is null
     *
     * @param id the id of the walletTransaction to save.
     * @param walletTransaction the walletTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walletTransaction,
     * or with status {@code 400 (Bad Request)} if the walletTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the walletTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the walletTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WalletTransaction> partialUpdateWalletTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WalletTransaction walletTransaction
    ) throws URISyntaxException {
        log.debug("REST request to partial update WalletTransaction partially : {}, {}", id, walletTransaction);
        if (walletTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walletTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walletTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WalletTransaction> result = walletTransactionRepository
            .findById(walletTransaction.getId())
            .map(existingWalletTransaction -> {
                if (walletTransaction.getTransID() != null) {
                    existingWalletTransaction.setTransID(walletTransaction.getTransID());
                }
                if (walletTransaction.getAmount() != null) {
                    existingWalletTransaction.setAmount(walletTransaction.getAmount());
                }
                if (walletTransaction.getDate() != null) {
                    existingWalletTransaction.setDate(walletTransaction.getDate());
                }
                if (walletTransaction.getWalletType() != null) {
                    existingWalletTransaction.setWalletType(walletTransaction.getWalletType());
                }
                if (walletTransaction.getTransStatus() != null) {
                    existingWalletTransaction.setTransStatus(walletTransaction.getTransStatus());
                }
                if (walletTransaction.getTransactionThirdPartyID() != null) {
                    existingWalletTransaction.setTransactionThirdPartyID(walletTransaction.getTransactionThirdPartyID());
                }

                return existingWalletTransaction;
            })
            .map(walletTransactionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walletTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /wallet-transactions} : get all the walletTransactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of walletTransactions in body.
     */
    @GetMapping("")
    public List<WalletTransaction> getAllWalletTransactions() {
        log.debug("REST request to get all WalletTransactions");
        return walletTransactionRepository.findAll();
    }

    /**
     * {@code GET  /wallet-transactions/:id} : get the "id" walletTransaction.
     *
     * @param id the id of the walletTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the walletTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WalletTransaction> getWalletTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to get WalletTransaction : {}", id);
        Optional<WalletTransaction> walletTransaction = walletTransactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(walletTransaction);
    }

    /**
     * {@code DELETE  /wallet-transactions/:id} : delete the "id" walletTransaction.
     *
     * @param id the id of the walletTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWalletTransaction(@PathVariable("id") Long id) {
        log.debug("REST request to delete WalletTransaction : {}", id);
        walletTransactionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
