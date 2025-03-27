package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.TripStopLocationAsserts.*;
import static strip.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import strip.domain.TripStopLocation;
import strip.repository.TripStopLocationRepository;
import strip.service.dto.TripStopLocationDTO;
import strip.service.mapper.TripStopLocationMapper;

/**
 * Integration tests for the {@link TripStopLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripStopLocationResourceIT {

    private static final UUID DEFAULT_STOP_LOCA_ID = UUID.randomUUID();
    private static final UUID UPDATED_STOP_LOCA_ID = UUID.randomUUID();

    private static final String DEFAULT_STOP_LOCA = "AAAAAAAAAA";
    private static final String UPDATED_STOP_LOCA = "BBBBBBBBBB";

    private static final Instant DEFAULT_STOP_LOCA_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STOP_LOCA_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STOP_LOCA_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STOP_LOCA_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trip-stop-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TripStopLocationRepository tripStopLocationRepository;

    @Autowired
    private TripStopLocationMapper tripStopLocationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripStopLocationMockMvc;

    private TripStopLocation tripStopLocation;

    private TripStopLocation insertedTripStopLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripStopLocation createEntity() {
        return new TripStopLocation()
            .stopLocaID(DEFAULT_STOP_LOCA_ID)
            .stopLoca(DEFAULT_STOP_LOCA)
            .stopLocaTime(DEFAULT_STOP_LOCA_TIME)
            .stopLocaStatus(DEFAULT_STOP_LOCA_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripStopLocation createUpdatedEntity() {
        return new TripStopLocation()
            .stopLocaID(UPDATED_STOP_LOCA_ID)
            .stopLoca(UPDATED_STOP_LOCA)
            .stopLocaTime(UPDATED_STOP_LOCA_TIME)
            .stopLocaStatus(UPDATED_STOP_LOCA_STATUS);
    }

    @BeforeEach
    public void initTest() {
        tripStopLocation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedTripStopLocation != null) {
            tripStopLocationRepository.delete(insertedTripStopLocation);
            insertedTripStopLocation = null;
        }
    }

    @Test
    @Transactional
    void createTripStopLocation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);
        var returnedTripStopLocationDTO = om.readValue(
            restTripStopLocationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripStopLocationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TripStopLocationDTO.class
        );

        // Validate the TripStopLocation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTripStopLocation = tripStopLocationMapper.toEntity(returnedTripStopLocationDTO);
        assertTripStopLocationUpdatableFieldsEquals(returnedTripStopLocation, getPersistedTripStopLocation(returnedTripStopLocation));

        insertedTripStopLocation = returnedTripStopLocation;
    }

    @Test
    @Transactional
    void createTripStopLocationWithExistingId() throws Exception {
        // Create the TripStopLocation with an existing ID
        tripStopLocation.setId(1L);
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripStopLocationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripStopLocationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripStopLocations() throws Exception {
        // Initialize the database
        insertedTripStopLocation = tripStopLocationRepository.saveAndFlush(tripStopLocation);

        // Get all the tripStopLocationList
        restTripStopLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripStopLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].stopLocaID").value(hasItem(DEFAULT_STOP_LOCA_ID.toString())))
            .andExpect(jsonPath("$.[*].stopLoca").value(hasItem(DEFAULT_STOP_LOCA)))
            .andExpect(jsonPath("$.[*].stopLocaTime").value(hasItem(DEFAULT_STOP_LOCA_TIME.toString())))
            .andExpect(jsonPath("$.[*].stopLocaStatus").value(hasItem(DEFAULT_STOP_LOCA_STATUS)));
    }

    @Test
    @Transactional
    void getTripStopLocation() throws Exception {
        // Initialize the database
        insertedTripStopLocation = tripStopLocationRepository.saveAndFlush(tripStopLocation);

        // Get the tripStopLocation
        restTripStopLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, tripStopLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripStopLocation.getId().intValue()))
            .andExpect(jsonPath("$.stopLocaID").value(DEFAULT_STOP_LOCA_ID.toString()))
            .andExpect(jsonPath("$.stopLoca").value(DEFAULT_STOP_LOCA))
            .andExpect(jsonPath("$.stopLocaTime").value(DEFAULT_STOP_LOCA_TIME.toString()))
            .andExpect(jsonPath("$.stopLocaStatus").value(DEFAULT_STOP_LOCA_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingTripStopLocation() throws Exception {
        // Get the tripStopLocation
        restTripStopLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTripStopLocation() throws Exception {
        // Initialize the database
        insertedTripStopLocation = tripStopLocationRepository.saveAndFlush(tripStopLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripStopLocation
        TripStopLocation updatedTripStopLocation = tripStopLocationRepository.findById(tripStopLocation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTripStopLocation are not directly saved in db
        em.detach(updatedTripStopLocation);
        updatedTripStopLocation
            .stopLocaID(UPDATED_STOP_LOCA_ID)
            .stopLoca(UPDATED_STOP_LOCA)
            .stopLocaTime(UPDATED_STOP_LOCA_TIME)
            .stopLocaStatus(UPDATED_STOP_LOCA_STATUS);
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(updatedTripStopLocation);

        restTripStopLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripStopLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripStopLocationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTripStopLocationToMatchAllProperties(updatedTripStopLocation);
    }

    @Test
    @Transactional
    void putNonExistingTripStopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripStopLocation.setId(longCount.incrementAndGet());

        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripStopLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripStopLocationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripStopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripStopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripStopLocation.setId(longCount.incrementAndGet());

        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripStopLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tripStopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripStopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripStopLocation.setId(longCount.incrementAndGet());

        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripStopLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tripStopLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripStopLocationWithPatch() throws Exception {
        // Initialize the database
        insertedTripStopLocation = tripStopLocationRepository.saveAndFlush(tripStopLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripStopLocation using partial update
        TripStopLocation partialUpdatedTripStopLocation = new TripStopLocation();
        partialUpdatedTripStopLocation.setId(tripStopLocation.getId());

        partialUpdatedTripStopLocation.stopLocaID(UPDATED_STOP_LOCA_ID);

        restTripStopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripStopLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTripStopLocation))
            )
            .andExpect(status().isOk());

        // Validate the TripStopLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripStopLocationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTripStopLocation, tripStopLocation),
            getPersistedTripStopLocation(tripStopLocation)
        );
    }

    @Test
    @Transactional
    void fullUpdateTripStopLocationWithPatch() throws Exception {
        // Initialize the database
        insertedTripStopLocation = tripStopLocationRepository.saveAndFlush(tripStopLocation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tripStopLocation using partial update
        TripStopLocation partialUpdatedTripStopLocation = new TripStopLocation();
        partialUpdatedTripStopLocation.setId(tripStopLocation.getId());

        partialUpdatedTripStopLocation
            .stopLocaID(UPDATED_STOP_LOCA_ID)
            .stopLoca(UPDATED_STOP_LOCA)
            .stopLocaTime(UPDATED_STOP_LOCA_TIME)
            .stopLocaStatus(UPDATED_STOP_LOCA_STATUS);

        restTripStopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripStopLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTripStopLocation))
            )
            .andExpect(status().isOk());

        // Validate the TripStopLocation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTripStopLocationUpdatableFieldsEquals(
            partialUpdatedTripStopLocation,
            getPersistedTripStopLocation(partialUpdatedTripStopLocation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTripStopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripStopLocation.setId(longCount.incrementAndGet());

        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripStopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripStopLocationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripStopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripStopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripStopLocation.setId(longCount.incrementAndGet());

        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripStopLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tripStopLocationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripStopLocation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tripStopLocation.setId(longCount.incrementAndGet());

        // Create the TripStopLocation
        TripStopLocationDTO tripStopLocationDTO = tripStopLocationMapper.toDto(tripStopLocation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripStopLocationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tripStopLocationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripStopLocation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripStopLocation() throws Exception {
        // Initialize the database
        insertedTripStopLocation = tripStopLocationRepository.saveAndFlush(tripStopLocation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tripStopLocation
        restTripStopLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripStopLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tripStopLocationRepository.count();
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

    protected TripStopLocation getPersistedTripStopLocation(TripStopLocation tripStopLocation) {
        return tripStopLocationRepository.findById(tripStopLocation.getId()).orElseThrow();
    }

    protected void assertPersistedTripStopLocationToMatchAllProperties(TripStopLocation expectedTripStopLocation) {
        assertTripStopLocationAllPropertiesEquals(expectedTripStopLocation, getPersistedTripStopLocation(expectedTripStopLocation));
    }

    protected void assertPersistedTripStopLocationToMatchUpdatableProperties(TripStopLocation expectedTripStopLocation) {
        assertTripStopLocationAllUpdatablePropertiesEquals(
            expectedTripStopLocation,
            getPersistedTripStopLocation(expectedTripStopLocation)
        );
    }
}
