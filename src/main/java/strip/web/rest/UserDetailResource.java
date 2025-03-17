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
import strip.domain.UserDetail;
import strip.repository.UserDetailRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.UserDetail}.
 */

@RestController
@RequestMapping("/api/user-details")
@Transactional
public class UserDetailResource {

    private final Logger log = LoggerFactory.getLogger(UserDetailResource.class);

    private static final String ENTITY_NAME = "userDetail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDetailRepository userDetailRepository;

    public UserDetailResource(UserDetailRepository userDetailRepository) {
        this.userDetailRepository = userDetailRepository;
    }

    /**
     * {@code POST  /user-details} : Create a new userDetail.
     *
     * @param userDetail the userDetail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDetail, or with status {@code 400 (Bad Request)} if the userDetail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<UserDetail> createUserDetail(@RequestBody UserDetail userDetail) throws URISyntaxException {
        log.debug("REST request to save UserDetail : {}", userDetail);
        if (userDetail.getId() != null) {
            throw new BadRequestAlertException("A new userDetail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userDetail = userDetailRepository.save(userDetail);
        return ResponseEntity.created(new URI("/api/user-details/" + userDetail.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userDetail.getId().toString()))
            .body(userDetail);
    }

    /**
     * {@code PUT  /user-details/:id} : Updates an existing userDetail.
     *
     * @param id the id of the userDetail to save.
     * @param userDetail the userDetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetail,
     * or with status {@code 400 (Bad Request)} if the userDetail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<UserDetail> updateUserDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDetail userDetail
    ) throws URISyntaxException {
        log.debug("REST request to update UserDetail : {}, {}", id, userDetail);
        if (userDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userDetail = userDetailRepository.save(userDetail);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDetail.getId().toString()))
            .body(userDetail);
    }

    /**
     * {@code PATCH  /user-details/:id} : Partial updates given fields of an existing userDetail, field will ignore if it is null
     *
     * @param id the id of the userDetail to save.
     * @param userDetail the userDetail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDetail,
     * or with status {@code 400 (Bad Request)} if the userDetail is not valid,
     * or with status {@code 404 (Not Found)} if the userDetail is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDetail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserDetail> partialUpdateUserDetail(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDetail userDetail
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserDetail partially : {}, {}", id, userDetail);
        if (userDetail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDetail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDetailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDetail> result = userDetailRepository
            .findById(userDetail.getId())
            .map(existingUserDetail -> {
                if (userDetail.getAppUserDetail() != null) {
                    existingUserDetail.setAppUserDetail(userDetail.getAppUserDetail());
                }
                if (userDetail.getUserimage() != null) {
                    existingUserDetail.setUserimage(userDetail.getUserimage());
                }
                if (userDetail.getUserimageContentType() != null) {
                    existingUserDetail.setUserimageContentType(userDetail.getUserimageContentType());
                }
                if (userDetail.getPhone() != null) {
                    existingUserDetail.setPhone(userDetail.getPhone());
                }
                if (userDetail.getGender() != null) {
                    existingUserDetail.setGender(userDetail.getGender());
                }
                if (userDetail.getAddress() != null) {
                    existingUserDetail.setAddress(userDetail.getAddress());
                }
                if (userDetail.getDob() != null) {
                    existingUserDetail.setDob(userDetail.getDob());
                }

                return existingUserDetail;
            })
            .map(userDetailRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDetail.getId().toString())
        );
    }

    /**
     * {@code GET  /user-details} : get all the userDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDetails in body.
     */
    @Hidden
    @GetMapping("")
    public List<UserDetail> getAllUserDetails() {
        log.debug("REST request to get all UserDetails");
        return userDetailRepository.findAll();
    }

    /**
     * {@code GET  /user-details/:id} : get the "id" userDetail.
     *
     * @param id the id of the userDetail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDetail, or with status {@code 404 (Not Found)}.
     */
    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<UserDetail> getUserDetail(@PathVariable("id") Long id) {
        log.debug("REST request to get UserDetail : {}", id);
        Optional<UserDetail> userDetail = userDetailRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userDetail);
    }

    /**
     * {@code DELETE  /user-details/:id} : delete the "id" userDetail.
     *
     * @param id the id of the userDetail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDetail(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserDetail : {}", id);
        userDetailRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
