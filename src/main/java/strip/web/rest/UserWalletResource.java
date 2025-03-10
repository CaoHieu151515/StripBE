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
import strip.domain.UserWallet;
import strip.repository.UserWalletRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.UserWallet}.
 */
@RestController
@RequestMapping("/api/user-wallets")
@Transactional
public class UserWalletResource {

    private final Logger log = LoggerFactory.getLogger(UserWalletResource.class);

    private static final String ENTITY_NAME = "userWallet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserWalletRepository userWalletRepository;

    public UserWalletResource(UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
    }

    /**
     * {@code POST  /user-wallets} : Create a new userWallet.
     *
     * @param userWallet the userWallet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userWallet, or with status {@code 400 (Bad Request)} if the userWallet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserWallet> createUserWallet(@RequestBody UserWallet userWallet) throws URISyntaxException {
        log.debug("REST request to save UserWallet : {}", userWallet);
        if (userWallet.getId() != null) {
            throw new BadRequestAlertException("A new userWallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userWallet = userWalletRepository.save(userWallet);
        return ResponseEntity.created(new URI("/api/user-wallets/" + userWallet.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userWallet.getId().toString()))
            .body(userWallet);
    }

    /**
     * {@code PUT  /user-wallets/:id} : Updates an existing userWallet.
     *
     * @param id the id of the userWallet to save.
     * @param userWallet the userWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userWallet,
     * or with status {@code 400 (Bad Request)} if the userWallet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserWallet> updateUserWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserWallet userWallet
    ) throws URISyntaxException {
        log.debug("REST request to update UserWallet : {}, {}", id, userWallet);
        if (userWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userWallet = userWalletRepository.save(userWallet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userWallet.getId().toString()))
            .body(userWallet);
    }

    /**
     * {@code PATCH  /user-wallets/:id} : Partial updates given fields of an existing userWallet, field will ignore if it is null
     *
     * @param id the id of the userWallet to save.
     * @param userWallet the userWallet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userWallet,
     * or with status {@code 400 (Bad Request)} if the userWallet is not valid,
     * or with status {@code 404 (Not Found)} if the userWallet is not found,
     * or with status {@code 500 (Internal Server Error)} if the userWallet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserWallet> partialUpdateUserWallet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserWallet userWallet
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserWallet partially : {}, {}", id, userWallet);
        if (userWallet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userWallet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userWalletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserWallet> result = userWalletRepository
            .findById(userWallet.getId())
            .map(existingUserWallet -> {
                if (userWallet.getUserWallet() != null) {
                    existingUserWallet.setUserWallet(userWallet.getUserWallet());
                }
                if (userWallet.getBefore() != null) {
                    existingUserWallet.setBefore(userWallet.getBefore());
                }
                if (userWallet.getAmount() != null) {
                    existingUserWallet.setAmount(userWallet.getAmount());
                }
                if (userWallet.getCurrent() != null) {
                    existingUserWallet.setCurrent(userWallet.getCurrent());
                }
                if (userWallet.getMobifyDate() != null) {
                    existingUserWallet.setMobifyDate(userWallet.getMobifyDate());
                }

                return existingUserWallet;
            })
            .map(userWalletRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userWallet.getId().toString())
        );
    }

    /**
     * {@code GET  /user-wallets} : get all the userWallets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userWallets in body.
     */
    @GetMapping("")
    public List<UserWallet> getAllUserWallets() {
        log.debug("REST request to get all UserWallets");
        return userWalletRepository.findAll();
    }

    /**
     * {@code GET  /user-wallets/:id} : get the "id" userWallet.
     *
     * @param id the id of the userWallet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userWallet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserWallet> getUserWallet(@PathVariable("id") Long id) {
        log.debug("REST request to get UserWallet : {}", id);
        Optional<UserWallet> userWallet = userWalletRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userWallet);
    }

    /**
     * {@code DELETE  /user-wallets/:id} : delete the "id" userWallet.
     *
     * @param id the id of the userWallet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserWallet(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserWallet : {}", id);
        userWalletRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
