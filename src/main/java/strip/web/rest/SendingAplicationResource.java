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
import strip.domain.SendingAplication;
import strip.repository.SendingAplicationRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.SendingAplication}.
 */
@RestController
@RequestMapping("/api/sending-aplications")
@Transactional
public class SendingAplicationResource {

    private final Logger log = LoggerFactory.getLogger(SendingAplicationResource.class);

    private static final String ENTITY_NAME = "sendingAplication";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SendingAplicationRepository sendingAplicationRepository;

    public SendingAplicationResource(SendingAplicationRepository sendingAplicationRepository) {
        this.sendingAplicationRepository = sendingAplicationRepository;
    }

    /**
     * {@code POST  /sending-aplications} : Create a new sendingAplication.
     *
     * @param sendingAplication the sendingAplication to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sendingAplication, or with status {@code 400 (Bad Request)} if the sendingAplication has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SendingAplication> createSendingAplication(@RequestBody SendingAplication sendingAplication)
        throws URISyntaxException {
        log.debug("REST request to save SendingAplication : {}", sendingAplication);
        if (sendingAplication.getId() != null) {
            throw new BadRequestAlertException("A new sendingAplication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sendingAplication = sendingAplicationRepository.save(sendingAplication);
        return ResponseEntity.created(new URI("/api/sending-aplications/" + sendingAplication.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, sendingAplication.getId().toString()))
            .body(sendingAplication);
    }

    /**
     * {@code PUT  /sending-aplications/:id} : Updates an existing sendingAplication.
     *
     * @param id the id of the sendingAplication to save.
     * @param sendingAplication the sendingAplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sendingAplication,
     * or with status {@code 400 (Bad Request)} if the sendingAplication is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sendingAplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SendingAplication> updateSendingAplication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SendingAplication sendingAplication
    ) throws URISyntaxException {
        log.debug("REST request to update SendingAplication : {}, {}", id, sendingAplication);
        if (sendingAplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sendingAplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sendingAplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        sendingAplication = sendingAplicationRepository.save(sendingAplication);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sendingAplication.getId().toString()))
            .body(sendingAplication);
    }

    /**
     * {@code PATCH  /sending-aplications/:id} : Partial updates given fields of an existing sendingAplication, field will ignore if it is null
     *
     * @param id the id of the sendingAplication to save.
     * @param sendingAplication the sendingAplication to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sendingAplication,
     * or with status {@code 400 (Bad Request)} if the sendingAplication is not valid,
     * or with status {@code 404 (Not Found)} if the sendingAplication is not found,
     * or with status {@code 500 (Internal Server Error)} if the sendingAplication couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SendingAplication> partialUpdateSendingAplication(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SendingAplication sendingAplication
    ) throws URISyntaxException {
        log.debug("REST request to partial update SendingAplication partially : {}, {}", id, sendingAplication);
        if (sendingAplication.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sendingAplication.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sendingAplicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SendingAplication> result = sendingAplicationRepository
            .findById(sendingAplication.getId())
            .map(existingSendingAplication -> {
                if (sendingAplication.getApliID() != null) {
                    existingSendingAplication.setApliID(sendingAplication.getApliID());
                }
                if (sendingAplication.getSendApplicationType() != null) {
                    existingSendingAplication.setSendApplicationType(sendingAplication.getSendApplicationType());
                }
                if (sendingAplication.getContent() != null) {
                    existingSendingAplication.setContent(sendingAplication.getContent());
                }
                if (sendingAplication.getImg() != null) {
                    existingSendingAplication.setImg(sendingAplication.getImg());
                }
                if (sendingAplication.getImgContentType() != null) {
                    existingSendingAplication.setImgContentType(sendingAplication.getImgContentType());
                }

                return existingSendingAplication;
            })
            .map(sendingAplicationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sendingAplication.getId().toString())
        );
    }

    /**
     * {@code GET  /sending-aplications} : get all the sendingAplications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sendingAplications in body.
     */
    @GetMapping("")
    public List<SendingAplication> getAllSendingAplications() {
        log.debug("REST request to get all SendingAplications");
        return sendingAplicationRepository.findAll();
    }

    /**
     * {@code GET  /sending-aplications/:id} : get the "id" sendingAplication.
     *
     * @param id the id of the sendingAplication to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sendingAplication, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SendingAplication> getSendingAplication(@PathVariable("id") Long id) {
        log.debug("REST request to get SendingAplication : {}", id);
        Optional<SendingAplication> sendingAplication = sendingAplicationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sendingAplication);
    }

    /**
     * {@code DELETE  /sending-aplications/:id} : delete the "id" sendingAplication.
     *
     * @param id the id of the sendingAplication to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSendingAplication(@PathVariable("id") Long id) {
        log.debug("REST request to delete SendingAplication : {}", id);
        sendingAplicationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
