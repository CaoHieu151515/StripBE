package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.SystemTempWalletAsserts.*;
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
import strip.domain.SystemTempWallet;
import strip.repository.SystemTempWalletRepository;

/**
 * Integration tests for the {@link SystemTempWalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemTempWalletResourceIT {

    private static final UUID DEFAULT_SYSTEM_WALLET_ID = UUID.randomUUID();
    private static final UUID UPDATED_SYSTEM_WALLET_ID = UUID.randomUUID();

    private static final Double DEFAULT_BEFORE = 1D;
    private static final Double UPDATED_BEFORE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_CURRENT = 1D;
    private static final Double UPDATED_CURRENT = 2D;

    private static final Instant DEFAULT_MOBIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MOBIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-temp-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemTempWalletRepository systemTempWalletRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemTempWalletMockMvc;

    private SystemTempWallet systemTempWallet;

    private SystemTempWallet insertedSystemTempWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemTempWallet createEntity(EntityManager em) {
        SystemTempWallet systemTempWallet = new SystemTempWallet()
            .systemWalletID(DEFAULT_SYSTEM_WALLET_ID)
            .before(DEFAULT_BEFORE)
            .amount(DEFAULT_AMOUNT)
            .current(DEFAULT_CURRENT)
            .mobifyDate(DEFAULT_MOBIFY_DATE);
        return systemTempWallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemTempWallet createUpdatedEntity(EntityManager em) {
        SystemTempWallet systemTempWallet = new SystemTempWallet()
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);
        return systemTempWallet;
    }

    @BeforeEach
    public void initTest() {
        systemTempWallet = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSystemTempWallet != null) {
            systemTempWalletRepository.delete(insertedSystemTempWallet);
            insertedSystemTempWallet = null;
        }
    }

    @Test
    @Transactional
    void createSystemTempWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemTempWallet
        var returnedSystemTempWallet = om.readValue(
            restSystemTempWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemTempWallet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemTempWallet.class
        );

        // Validate the SystemTempWallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSystemTempWalletUpdatableFieldsEquals(returnedSystemTempWallet, getPersistedSystemTempWallet(returnedSystemTempWallet));

        insertedSystemTempWallet = returnedSystemTempWallet;
    }

    @Test
    @Transactional
    void createSystemTempWalletWithExistingId() throws Exception {
        // Create the SystemTempWallet with an existing ID
        systemTempWallet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemTempWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemTempWallet)))
            .andExpect(status().isBadRequest());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSystemTempWallets() throws Exception {
        // Initialize the database
        insertedSystemTempWallet = systemTempWalletRepository.saveAndFlush(systemTempWallet);

        // Get all the systemTempWalletList
        restSystemTempWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemTempWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].systemWalletID").value(hasItem(DEFAULT_SYSTEM_WALLET_ID.toString())))
            .andExpect(jsonPath("$.[*].before").value(hasItem(DEFAULT_BEFORE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.doubleValue())))
            .andExpect(jsonPath("$.[*].mobifyDate").value(hasItem(DEFAULT_MOBIFY_DATE.toString())));
    }

    @Test
    @Transactional
    void getSystemTempWallet() throws Exception {
        // Initialize the database
        insertedSystemTempWallet = systemTempWalletRepository.saveAndFlush(systemTempWallet);

        // Get the systemTempWallet
        restSystemTempWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, systemTempWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemTempWallet.getId().intValue()))
            .andExpect(jsonPath("$.systemWalletID").value(DEFAULT_SYSTEM_WALLET_ID.toString()))
            .andExpect(jsonPath("$.before").value(DEFAULT_BEFORE.doubleValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.doubleValue()))
            .andExpect(jsonPath("$.mobifyDate").value(DEFAULT_MOBIFY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemTempWallet() throws Exception {
        // Get the systemTempWallet
        restSystemTempWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemTempWallet() throws Exception {
        // Initialize the database
        insertedSystemTempWallet = systemTempWalletRepository.saveAndFlush(systemTempWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemTempWallet
        SystemTempWallet updatedSystemTempWallet = systemTempWalletRepository.findById(systemTempWallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemTempWallet are not directly saved in db
        em.detach(updatedSystemTempWallet);
        updatedSystemTempWallet
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restSystemTempWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemTempWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSystemTempWallet))
            )
            .andExpect(status().isOk());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemTempWalletToMatchAllProperties(updatedSystemTempWallet);
    }

    @Test
    @Transactional
    void putNonExistingSystemTempWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemTempWallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemTempWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemTempWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemTempWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemTempWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemTempWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemTempWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemTempWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemTempWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemTempWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemTempWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemTempWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemTempWalletWithPatch() throws Exception {
        // Initialize the database
        insertedSystemTempWallet = systemTempWalletRepository.saveAndFlush(systemTempWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemTempWallet using partial update
        SystemTempWallet partialUpdatedSystemTempWallet = new SystemTempWallet();
        partialUpdatedSystemTempWallet.setId(systemTempWallet.getId());

        partialUpdatedSystemTempWallet.before(UPDATED_BEFORE).current(UPDATED_CURRENT);

        restSystemTempWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemTempWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemTempWallet))
            )
            .andExpect(status().isOk());

        // Validate the SystemTempWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemTempWalletUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemTempWallet, systemTempWallet),
            getPersistedSystemTempWallet(systemTempWallet)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemTempWalletWithPatch() throws Exception {
        // Initialize the database
        insertedSystemTempWallet = systemTempWalletRepository.saveAndFlush(systemTempWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemTempWallet using partial update
        SystemTempWallet partialUpdatedSystemTempWallet = new SystemTempWallet();
        partialUpdatedSystemTempWallet.setId(systemTempWallet.getId());

        partialUpdatedSystemTempWallet
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restSystemTempWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemTempWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemTempWallet))
            )
            .andExpect(status().isOk());

        // Validate the SystemTempWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemTempWalletUpdatableFieldsEquals(
            partialUpdatedSystemTempWallet,
            getPersistedSystemTempWallet(partialUpdatedSystemTempWallet)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSystemTempWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemTempWallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemTempWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemTempWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemTempWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemTempWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemTempWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemTempWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemTempWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemTempWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemTempWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemTempWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemTempWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemTempWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemTempWallet() throws Exception {
        // Initialize the database
        insertedSystemTempWallet = systemTempWalletRepository.saveAndFlush(systemTempWallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemTempWallet
        restSystemTempWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemTempWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemTempWalletRepository.count();
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

    protected SystemTempWallet getPersistedSystemTempWallet(SystemTempWallet systemTempWallet) {
        return systemTempWalletRepository.findById(systemTempWallet.getId()).orElseThrow();
    }

    protected void assertPersistedSystemTempWalletToMatchAllProperties(SystemTempWallet expectedSystemTempWallet) {
        assertSystemTempWalletAllPropertiesEquals(expectedSystemTempWallet, getPersistedSystemTempWallet(expectedSystemTempWallet));
    }

    protected void assertPersistedSystemTempWalletToMatchUpdatableProperties(SystemTempWallet expectedSystemTempWallet) {
        assertSystemTempWalletAllUpdatablePropertiesEquals(
            expectedSystemTempWallet,
            getPersistedSystemTempWallet(expectedSystemTempWallet)
        );
    }
}
