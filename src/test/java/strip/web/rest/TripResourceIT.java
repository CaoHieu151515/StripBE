package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.TripAsserts.*;
import static strip.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import strip.IntegrationTest;
import strip.domain.Trip;
import strip.domain.enumeration.TripStatus;
import strip.repository.TripRepository;
import strip.service.dto.TripDTO;
import strip.service.mapper.TripMapper;

/**
 * Integration tests for the {@link TripResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripResourceIT {

    private static final UUID DEFAULT_TRIP_ID = UUID.randomUUID();
    private static final UUID UPDATED_TRIP_ID = UUID.randomUUID();

    private static final byte[] DEFAULT_TRIP_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_TRIP_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_TRIP_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_TRIP_IMG_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_PRICE_PER_SEAT = 1D;
    private static final Double UPDATED_PRICE_PER_SEAT = 2D;

    private static final Integer DEFAULT_MAX_SEAT = 1;
    private static final Integer UPDATED_MAX_SEAT = 2;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_CURRENT_SEAT = 1;
    private static final Integer UPDATED_CURRENT_SEAT = 2;

    private static final String DEFAULT_START_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION = "BBBBBBBBBB";

    private static final String DEFAULT_CANCEL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CANCEL_REASON = "BBBBBBBBBB";

    private static final TripStatus DEFAULT_TRIP_STATUS = TripStatus.UPCOMING;
    private static final TripStatus UPDATED_TRIP_STATUS = TripStatus.CONFIRMING;

    private static final String ENTITY_API_URL = "/api/trips";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripMapper tripMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripMockMvc;

    private Trip trip;

    private Trip insertedTrip;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createEntity() {
        return new Trip()
            .tripID(DEFAULT_TRIP_ID)
            .tripImg(DEFAULT_TRIP_IMG)
            .tripImgContentType(DEFAULT_TRIP_IMG_CONTENT_TYPE)
            .pricePerSeat(DEFAULT_PRICE_PER_SEAT)
            .maxSeat(DEFAULT_MAX_SEAT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .currentSeat(DEFAULT_CURRENT_SEAT)
            .startLocation(DEFAULT_START_LOCATION)
            .endLocation(DEFAULT_END_LOCATION)
            .description(DEFAULT_DESCRIPTION)
            .condition(DEFAULT_CONDITION)
            .cancelReason(DEFAULT_CANCEL_REASON)
            .tripStatus(DEFAULT_TRIP_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trip createUpdatedEntity() {
        return new Trip()
            .tripID(UPDATED_TRIP_ID)
            .tripImg(UPDATED_TRIP_IMG)
            .tripImgContentType(UPDATED_TRIP_IMG_CONTENT_TYPE)
            .pricePerSeat(UPDATED_PRICE_PER_SEAT)
            .maxSeat(UPDATED_MAX_SEAT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .currentSeat(UPDATED_CURRENT_SEAT)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .condition(UPDATED_CONDITION)
            .cancelReason(UPDATED_CANCEL_REASON)
            .tripStatus(UPDATED_TRIP_STATUS);
    }

    @BeforeEach
    public void initTest() {
        trip = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTrip != null) {
            tripRepository.delete(insertedTrip);
            insertedTrip = null;
        }
    }

    @Test
    @Transactional
    void createTrip() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);
        var returnedTripDTO = om.readValue(
            restTripMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TripDTO.class
        );

        // Validate the Trip in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrip = tripMapper.toEntity(returnedTripDTO);
        assertTripUpdatableFieldsEquals(returnedTrip, getPersistedTrip(returnedTrip));

        insertedTrip = returnedTrip;
    }

    @Test
    @Transactional
    void createTripWithExistingId() throws Exception {
        // Create the Trip with an existing ID
        trip.setId(1L);
        TripDTO tripDTO = tripMapper.toDto(trip);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrips() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get all the tripList
        restTripMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trip.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripID").value(hasItem(DEFAULT_TRIP_ID.toString())))
            .andExpect(jsonPath("$.[*].tripImgContentType").value(hasItem(DEFAULT_TRIP_IMG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].tripImg").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_TRIP_IMG))))
            .andExpect(jsonPath("$.[*].pricePerSeat").value(hasItem(DEFAULT_PRICE_PER_SEAT)))
            .andExpect(jsonPath("$.[*].maxSeat").value(hasItem(DEFAULT_MAX_SEAT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].currentSeat").value(hasItem(DEFAULT_CURRENT_SEAT)))
            .andExpect(jsonPath("$.[*].startLocation").value(hasItem(DEFAULT_START_LOCATION)))
            .andExpect(jsonPath("$.[*].endLocation").value(hasItem(DEFAULT_END_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].condition").value(hasItem(DEFAULT_CONDITION)))
            .andExpect(jsonPath("$.[*].cancelReason").value(hasItem(DEFAULT_CANCEL_REASON)))
            .andExpect(jsonPath("$.[*].tripStatus").value(hasItem(DEFAULT_TRIP_STATUS.toString())));
    }

    @Test
    @Transactional
    void getTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        // Get the trip
        restTripMockMvc
            .perform(get(ENTITY_API_URL_ID, trip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trip.getId().intValue()))
            .andExpect(jsonPath("$.tripID").value(DEFAULT_TRIP_ID.toString()))
            .andExpect(jsonPath("$.tripImgContentType").value(DEFAULT_TRIP_IMG_CONTENT_TYPE))
            .andExpect(jsonPath("$.tripImg").value(Base64.getEncoder().encodeToString(DEFAULT_TRIP_IMG)))
            .andExpect(jsonPath("$.pricePerSeat").value(DEFAULT_PRICE_PER_SEAT))
            .andExpect(jsonPath("$.maxSeat").value(DEFAULT_MAX_SEAT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.currentSeat").value(DEFAULT_CURRENT_SEAT))
            .andExpect(jsonPath("$.startLocation").value(DEFAULT_START_LOCATION))
            .andExpect(jsonPath("$.endLocation").value(DEFAULT_END_LOCATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.condition").value(DEFAULT_CONDITION))
            .andExpect(jsonPath("$.cancelReason").value(DEFAULT_CANCEL_REASON))
            .andExpect(jsonPath("$.tripStatus").value(DEFAULT_TRIP_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTrip() throws Exception {
        // Get the trip
        restTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip
        Trip updatedTrip = tripRepository.findById(trip.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrip are not directly saved in db
        em.detach(updatedTrip);
        updatedTrip
            .tripID(UPDATED_TRIP_ID)
            .tripImg(UPDATED_TRIP_IMG)
            .tripImgContentType(UPDATED_TRIP_IMG_CONTENT_TYPE)
            .pricePerSeat(UPDATED_PRICE_PER_SEAT)
            .maxSeat(UPDATED_MAX_SEAT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .currentSeat(UPDATED_CURRENT_SEAT)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .condition(UPDATED_CONDITION)
            .cancelReason(UPDATED_CANCEL_REASON)
            .tripStatus(UPDATED_TRIP_STATUS);
        TripDTO tripDTO = tripMapper.toDto(updatedTrip);

        restTripMockMvc
            .perform(put(ENTITY_API_URL_ID, tripDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isOk());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTripToMatchAllProperties(updatedTrip);
    }

    @Test
    @Transactional
    void putNonExistingTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL_ID, tripDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripWithPatch() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip
            .tripImg(UPDATED_TRIP_IMG)
            .tripImgContentType(UPDATED_TRIP_IMG_CONTENT_TYPE)
            .maxSeat(UPDATED_MAX_SEAT)
            .startLocation(UPDATED_START_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .condition(UPDATED_CONDITION);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTrip, trip), getPersistedTrip(trip));
    }

    @Test
    @Transactional
    void fullUpdateTripWithPatch() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trip using partial update
        Trip partialUpdatedTrip = new Trip();
        partialUpdatedTrip.setId(trip.getId());

        partialUpdatedTrip
            .tripID(UPDATED_TRIP_ID)
            .tripImg(UPDATED_TRIP_IMG)
            .tripImgContentType(UPDATED_TRIP_IMG_CONTENT_TYPE)
            .pricePerSeat(UPDATED_PRICE_PER_SEAT)
            .maxSeat(UPDATED_MAX_SEAT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .currentSeat(UPDATED_CURRENT_SEAT)
            .startLocation(UPDATED_START_LOCATION)
            .endLocation(UPDATED_END_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .condition(UPDATED_CONDITION)
            .cancelReason(UPDATED_CANCEL_REASON)
            .tripStatus(UPDATED_TRIP_STATUS);

        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrip.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrip))
            )
            .andExpect(status().isOk());

        // Validate the Trip in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripUpdatableFieldsEquals(partialUpdatedTrip, getPersistedTrip(partialUpdatedTrip));
    }

    @Test
    @Transactional
    void patchNonExistingTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrip() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trip.setId(longCount.incrementAndGet());

        // Create the Trip
        TripDTO tripDTO = tripMapper.toDto(trip);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trip in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrip() throws Exception {
        // Initialize the database
        insertedTrip = tripRepository.saveAndFlush(trip);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trip
        restTripMockMvc
            .perform(delete(ENTITY_API_URL_ID, trip.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tripRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Trip getPersistedTrip(Trip trip) {
        return tripRepository.findById(trip.getId()).orElseThrow();
    }

    protected void assertPersistedTripToMatchAllProperties(Trip expectedTrip) {
        assertTripAllPropertiesEquals(expectedTrip, getPersistedTrip(expectedTrip));
    }

    protected void assertPersistedTripToMatchUpdatableProperties(Trip expectedTrip) {
        assertTripAllUpdatablePropertiesEquals(expectedTrip, getPersistedTrip(expectedTrip));
    }
}
