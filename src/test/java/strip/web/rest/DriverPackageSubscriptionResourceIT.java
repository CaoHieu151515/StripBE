package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.DriverPackageSubscriptionAsserts.*;
import static strip.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
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
import strip.domain.DriverPackageSubscription;
import strip.repository.DriverPackageSubscriptionRepository;

/**
 * Integration tests for the {@link DriverPackageSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DriverPackageSubscriptionResourceIT {

    private static final Instant DEFAULT_PURCHASE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PURCHASE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_PACKAGE_PRICE = 1D;
    private static final Double UPDATED_PACKAGE_PRICE = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/driver-package-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DriverPackageSubscriptionRepository driverPackageSubscriptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriverPackageSubscriptionMockMvc;

    private DriverPackageSubscription driverPackageSubscription;

    private DriverPackageSubscription insertedDriverPackageSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DriverPackageSubscription createEntity() {
        return new DriverPackageSubscription()
            .purchaseDate(DEFAULT_PURCHASE_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .packagePrice(DEFAULT_PACKAGE_PRICE)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DriverPackageSubscription createUpdatedEntity() {
        return new DriverPackageSubscription()
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .packagePrice(UPDATED_PACKAGE_PRICE)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        driverPackageSubscription = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDriverPackageSubscription != null) {
            driverPackageSubscriptionRepository.delete(insertedDriverPackageSubscription);
            insertedDriverPackageSubscription = null;
        }
    }

    @Test
    @Transactional
    void createDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DriverPackageSubscription
        var returnedDriverPackageSubscription = om.readValue(
            restDriverPackageSubscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverPackageSubscription))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DriverPackageSubscription.class
        );

        // Validate the DriverPackageSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDriverPackageSubscriptionUpdatableFieldsEquals(
            returnedDriverPackageSubscription,
            getPersistedDriverPackageSubscription(returnedDriverPackageSubscription)
        );

        insertedDriverPackageSubscription = returnedDriverPackageSubscription;
    }

    @Test
    @Transactional
    void createDriverPackageSubscriptionWithExistingId() throws Exception {
        // Create the DriverPackageSubscription with an existing ID
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverPackageSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverPackageSubscription)))
            .andExpect(status().isBadRequest());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDriverPackageSubscriptions() throws Exception {
        // Initialize the database
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        // Get all the driverPackageSubscriptionList
        restDriverPackageSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driverPackageSubscription.getId().toString())))
            .andExpect(jsonPath("$.[*].purchaseDate").value(hasItem(DEFAULT_PURCHASE_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].packagePrice").value(hasItem(DEFAULT_PACKAGE_PRICE)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    void getDriverPackageSubscription() throws Exception {
        // Initialize the database
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        // Get the driverPackageSubscription
        restDriverPackageSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, driverPackageSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(driverPackageSubscription.getId().toString()))
            .andExpect(jsonPath("$.purchaseDate").value(DEFAULT_PURCHASE_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.packagePrice").value(DEFAULT_PACKAGE_PRICE))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingDriverPackageSubscription() throws Exception {
        // Get the driverPackageSubscription
        restDriverPackageSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDriverPackageSubscription() throws Exception {
        // Initialize the database
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driverPackageSubscription
        DriverPackageSubscription updatedDriverPackageSubscription = driverPackageSubscriptionRepository
            .findById(driverPackageSubscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDriverPackageSubscription are not directly saved in db
        em.detach(updatedDriverPackageSubscription);
        updatedDriverPackageSubscription
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .packagePrice(UPDATED_PACKAGE_PRICE)
            .active(UPDATED_ACTIVE);

        restDriverPackageSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDriverPackageSubscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDriverPackageSubscription))
            )
            .andExpect(status().isOk());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDriverPackageSubscriptionToMatchAllProperties(updatedDriverPackageSubscription);
    }

    @Test
    @Transactional
    void putNonExistingDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPackageSubscription.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverPackageSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverPackageSubscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(driverPackageSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPackageSubscription.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPackageSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(driverPackageSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPackageSubscription.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPackageSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverPackageSubscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriverPackageSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driverPackageSubscription using partial update
        DriverPackageSubscription partialUpdatedDriverPackageSubscription = new DriverPackageSubscription();
        partialUpdatedDriverPackageSubscription.setId(driverPackageSubscription.getId());

        partialUpdatedDriverPackageSubscription.packagePrice(UPDATED_PACKAGE_PRICE);

        restDriverPackageSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriverPackageSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriverPackageSubscription))
            )
            .andExpect(status().isOk());

        // Validate the DriverPackageSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverPackageSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDriverPackageSubscription, driverPackageSubscription),
            getPersistedDriverPackageSubscription(driverPackageSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateDriverPackageSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driverPackageSubscription using partial update
        DriverPackageSubscription partialUpdatedDriverPackageSubscription = new DriverPackageSubscription();
        partialUpdatedDriverPackageSubscription.setId(driverPackageSubscription.getId());

        partialUpdatedDriverPackageSubscription
            .purchaseDate(UPDATED_PURCHASE_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .packagePrice(UPDATED_PACKAGE_PRICE)
            .active(UPDATED_ACTIVE);

        restDriverPackageSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriverPackageSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriverPackageSubscription))
            )
            .andExpect(status().isOk());

        // Validate the DriverPackageSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverPackageSubscriptionUpdatableFieldsEquals(
            partialUpdatedDriverPackageSubscription,
            getPersistedDriverPackageSubscription(partialUpdatedDriverPackageSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPackageSubscription.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverPackageSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driverPackageSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverPackageSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPackageSubscription.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPackageSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverPackageSubscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDriverPackageSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driverPackageSubscription.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverPackageSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(driverPackageSubscription))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DriverPackageSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDriverPackageSubscription() throws Exception {
        // Initialize the database
        insertedDriverPackageSubscription = driverPackageSubscriptionRepository.saveAndFlush(driverPackageSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the driverPackageSubscription
        restDriverPackageSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, driverPackageSubscription.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return driverPackageSubscriptionRepository.count();
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

    protected DriverPackageSubscription getPersistedDriverPackageSubscription(DriverPackageSubscription driverPackageSubscription) {
        return driverPackageSubscriptionRepository.findById(driverPackageSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedDriverPackageSubscriptionToMatchAllProperties(
        DriverPackageSubscription expectedDriverPackageSubscription
    ) {
        assertDriverPackageSubscriptionAllPropertiesEquals(
            expectedDriverPackageSubscription,
            getPersistedDriverPackageSubscription(expectedDriverPackageSubscription)
        );
    }

    protected void assertPersistedDriverPackageSubscriptionToMatchUpdatableProperties(
        DriverPackageSubscription expectedDriverPackageSubscription
    ) {
        assertDriverPackageSubscriptionAllUpdatablePropertiesEquals(
            expectedDriverPackageSubscription,
            getPersistedDriverPackageSubscription(expectedDriverPackageSubscription)
        );
    }
}
