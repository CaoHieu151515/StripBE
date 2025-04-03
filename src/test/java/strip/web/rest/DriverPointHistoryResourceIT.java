package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.DriverPointHistoryAsserts.*;
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
import strip.domain.DriverPointHistory;
import strip.domain.enumeration.DriverPointHistoryStatus;
import strip.repository.DriverPointHistoryRepository;

/**
 * Integration tests for the {@link DriverPointHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DriverPointHistoryResourceIT {

    private static final UUID DEFAULT_POINT_ID = UUID.randomUUID();
    private static final UUID UPDATED_POINT_ID = UUID.randomUUID();

    private static final Integer DEFAULT_POINT = 1;
    private static final Integer UPDATED_POINT = 2;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DriverPointHistoryStatus DEFAULT_STATUS = DriverPointHistoryStatus.DONE;
    private static final DriverPointHistoryStatus UPDATED_STATUS = DriverPointHistoryStatus.REFUND;

    private static final String ENTITY_API_URL = "/api/driver-point-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DriverPointHistoryRepository driverPointHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriverPointHistoryMockMvc;

    private DriverPointHistory driverPointHistory;

    private DriverPointHistory insertedDriverPointHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DriverPointHistory createEntity() {
        return new DriverPointHistory()
            .pointId(DEFAULT_POINT_ID)
            .point(DEFAULT_POINT)
            .reason(DEFAULT_REASON)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DriverPointHistory createUpdatedEntity() {
        return new DriverPointHistory()
            .pointId(UPDATED_POINT_ID)
            .point(UPDATED_POINT)
            .reason(UPDATED_REASON)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        driverPointHistory = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDriverPointHistory != null) {
            driverPointHistoryRepository.delete(insertedDriverPointHistory);
            insertedDriverPointHistory = null;
        }
    }

    @Test
    @Transactional
    void createDriverPointHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DriverPointHistory
        var returnedDriverPointHistory = om.readValue(
            restDriverPointHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverPointHistory)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DriverPointHistory.class
        );

        // Validate the DriverPointHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDriverPointHistoryUpdatableFieldsEquals(
            returnedDriverPointHistory,
            getPersistedDriverPointHistory(returnedDriverPointHistory)
        );

        insertedDriverPointHistory = returnedDriverPointHistory;
    }

    @Test
    @Transactional
    void createDriverPointHistoryWithExistingId() throws Exception {
        // Create the DriverPointHistory with an existing ID
        driverPointHistory.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverPointHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverPointHistory)))
            .andExpect(status().isBadRequest());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDriverPointHistories() throws Exception {
        // Initialize the database
        insertedDriverPointHistory = driverPointHistoryRepository.saveAndFlush(driverPointHistory);

        // Get all the driverPointHistoryList
        restDriverPointHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driverPointHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].pointId").value(hasItem(DEFAULT_POINT_ID.toString())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDriverPointHistory() throws Exception {
        // Initialize the database
        insertedDriverPointHistory = driverPointHistoryRepository.saveAndFlush(driverPointHistory);

        // Get the driverPointHistory
        restDriverPointHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, driverPointHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(driverPointHistory.getId().intValue()))
            .andExpect(jsonPath("$.pointId").value(DEFAULT_POINT_ID.toString()))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDriverPointHistory() throws Exception {
        // Get the driverPointHistory
        restDriverPointHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDriverPointHistory() throws Exception {
        // Initialize the database
        insertedDriverPointHistory = driverPointHistoryRepository.saveAndFlush(driverPointHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driverPointHistory
        DriverPointHistory updatedDriverPointHistory = driverPointHistoryRepository.findById(driverPointHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDriverPointHistory are not directly saved in db
        em.detach(updatedDriverPointHistory);
        updatedDriverPointHistory
            .pointId(UPDATED_POINT_ID)
            .point(UPDATED_POINT)
            .reason(UPDATED_REASON)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restDriverPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDriverPointHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDriverPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDriverPointHistoryToMatchAllProperties(updatedDriverPointHistory);
    }

    @Test
    @Transactional
    void putNonExistingDriverPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPointHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverPointHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(driverPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDriverPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPointHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(driverPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDriverPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPointHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverPointHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriverPointHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedDriverPointHistory = driverPointHistoryRepository.saveAndFlush(driverPointHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driverPointHistory using partial update
        DriverPointHistory partialUpdatedDriverPointHistory = new DriverPointHistory();
        partialUpdatedDriverPointHistory.setId(driverPointHistory.getId());

        partialUpdatedDriverPointHistory.pointId(UPDATED_POINT_ID).point(UPDATED_POINT).reason(UPDATED_REASON).status(UPDATED_STATUS);

        restDriverPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriverPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriverPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the DriverPointHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverPointHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDriverPointHistory, driverPointHistory),
            getPersistedDriverPointHistory(driverPointHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateDriverPointHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedDriverPointHistory = driverPointHistoryRepository.saveAndFlush(driverPointHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driverPointHistory using partial update
        DriverPointHistory partialUpdatedDriverPointHistory = new DriverPointHistory();
        partialUpdatedDriverPointHistory.setId(driverPointHistory.getId());

        partialUpdatedDriverPointHistory
            .pointId(UPDATED_POINT_ID)
            .point(UPDATED_POINT)
            .reason(UPDATED_REASON)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restDriverPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriverPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriverPointHistory))
            )
            .andExpect(status().isOk());

        // Validate the DriverPointHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverPointHistoryUpdatableFieldsEquals(
            partialUpdatedDriverPointHistory,
            getPersistedDriverPointHistory(partialUpdatedDriverPointHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDriverPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPointHistory.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driverPointHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDriverPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPointHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverPointHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDriverPointHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPointHistory.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPointHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(driverPointHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DriverPointHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDriverPointHistory() throws Exception {
        // Initialize the database
        insertedDriverPointHistory = driverPointHistoryRepository.saveAndFlush(driverPointHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the driverPointHistory
        restDriverPointHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, driverPointHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return driverPointHistoryRepository.count();
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

    protected DriverPointHistory getPersistedDriverPointHistory(DriverPointHistory driverPointHistory) {
        return driverPointHistoryRepository.findById(driverPointHistory.getId()).orElseThrow();
    }

    protected void assertPersistedDriverPointHistoryToMatchAllProperties(DriverPointHistory expectedDriverPointHistory) {
        assertDriverPointHistoryAllPropertiesEquals(expectedDriverPointHistory, getPersistedDriverPointHistory(expectedDriverPointHistory));
    }

    protected void assertPersistedDriverPointHistoryToMatchUpdatableProperties(DriverPointHistory expectedDriverPointHistory) {
        assertDriverPointHistoryAllUpdatablePropertiesEquals(
            expectedDriverPointHistory,
            getPersistedDriverPointHistory(expectedDriverPointHistory)
        );
    }
}
