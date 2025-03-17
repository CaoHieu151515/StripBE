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
import strip.domain.Rating;
import strip.repository.RatingRepository;
import strip.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link strip.domain.Rating}.
 */
@RestController
@RequestMapping("/api/ratings")
@Transactional
public class RatingResource {

    private final Logger log = LoggerFactory.getLogger(RatingResource.class);

    private static final String ENTITY_NAME = "rating";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RatingRepository ratingRepository;

    public RatingResource(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * {@code POST  /ratings} : Create a new rating.
     *
     * @param rating the rating to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rating, or with status {@code 400 (Bad Request)} if the rating has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PostMapping("")
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) throws URISyntaxException {
        log.debug("REST request to save Rating : {}", rating);
        if (rating.getId() != null) {
            throw new BadRequestAlertException("A new rating cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rating = ratingRepository.save(rating);
        return ResponseEntity.created(new URI("/api/ratings/" + rating.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rating.getId().toString()))
            .body(rating);
    }

    /**
     * {@code PUT  /ratings/:id} : Updates an existing rating.
     *
     * @param id the id of the rating to save.
     * @param rating the rating to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rating,
     * or with status {@code 400 (Bad Request)} if the rating is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rating couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable(value = "id", required = false) final Long id, @RequestBody Rating rating)
        throws URISyntaxException {
        log.debug("REST request to update Rating : {}, {}", id, rating);
        if (rating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rating.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rating = ratingRepository.save(rating);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rating.getId().toString()))
            .body(rating);
    }

    /**
     * {@code PATCH  /ratings/:id} : Partial updates given fields of an existing rating, field will ignore if it is null
     *
     * @param id the id of the rating to save.
     * @param rating the rating to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rating,
     * or with status {@code 400 (Bad Request)} if the rating is not valid,
     * or with status {@code 404 (Not Found)} if the rating is not found,
     * or with status {@code 500 (Internal Server Error)} if the rating couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Hidden
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Rating> partialUpdateRating(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Rating rating
    ) throws URISyntaxException {
        log.debug("REST request to partial update Rating partially : {}, {}", id, rating);
        if (rating.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rating.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Rating> result = ratingRepository
            .findById(rating.getId())
            .map(existingRating -> {
                if (rating.getRatingID() != null) {
                    existingRating.setRatingID(rating.getRatingID());
                }
                if (rating.getRatingTime() != null) {
                    existingRating.setRatingTime(rating.getRatingTime());
                }
                if (rating.getRatingDriver() != null) {
                    existingRating.setRatingDriver(rating.getRatingDriver());
                }
                if (rating.getRatingType() != null) {
                    existingRating.setRatingType(rating.getRatingType());
                }

                return existingRating;
            })
            .map(ratingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rating.getId().toString())
        );
    }

    /**
     * {@code GET  /ratings} : get all the ratings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ratings in body.
     */
    @Hidden
    @GetMapping("")
    public List<Rating> getAllRatings() {
        log.debug("REST request to get all Ratings");
        return ratingRepository.findAll();
    }

    /**
     * {@code GET  /ratings/:id} : get the "id" rating.
     *
     * @param id the id of the rating to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rating, or with status {@code 404 (Not Found)}.
     */
    @Hidden
    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRating(@PathVariable("id") Long id) {
        log.debug("REST request to get Rating : {}", id);
        Optional<Rating> rating = ratingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rating);
    }

    /**
     * {@code DELETE  /ratings/:id} : delete the "id" rating.
     *
     * @param id the id of the rating to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @Hidden
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable("id") Long id) {
        log.debug("REST request to delete Rating : {}", id);
        ratingRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
