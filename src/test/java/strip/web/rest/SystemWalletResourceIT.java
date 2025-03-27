package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.SystemWalletAsserts.*;
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
import strip.domain.SystemWallet;
import strip.repository.SystemWalletRepository;

/**
 * Integration tests for the {@link SystemWalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemWalletResourceIT {

    private static final UUID DEFAULT_SYSTEM_WALLET_ID = UUID.randomUUID();
    private static final UUID UPDATED_SYSTEM_WALLET_ID = UUID.randomUUID();

    private static final Double DEFAULT_BEFORE = 1D;
    private static final Double UPDATED_BEFORE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_CURRENT = 1D;
    private static final Double UPDATED_CURRENT = 2D;

    private static final Double DEFAULT_BLOCK_AMOUNT = 1D;
    private static final Double UPDATED_BLOCK_AMOUNT = 2D;

    private static final Instant DEFAULT_MOBIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MOBIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemWalletRepository systemWalletRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemWalletMockMvc;

    private SystemWallet systemWallet;

    private SystemWallet insertedSystemWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemWallet createEntity() {
        return new SystemWallet()
            .systemWalletID(DEFAULT_SYSTEM_WALLET_ID)
            .before(DEFAULT_BEFORE)
            .amount(DEFAULT_AMOUNT)
            .current(DEFAULT_CURRENT)
            .blockAmount(DEFAULT_BLOCK_AMOUNT)
            .mobifyDate(DEFAULT_MOBIFY_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemWallet createUpdatedEntity() {
        return new SystemWallet()
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .blockAmount(UPDATED_BLOCK_AMOUNT)
            .mobifyDate(UPDATED_MOBIFY_DATE);
    }

    @BeforeEach
    public void initTest() {
        systemWallet = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSystemWallet != null) {
            systemWalletRepository.delete(insertedSystemWallet);
            insertedSystemWallet = null;
        }
    }

    @Test
    @Transactional
    void createSystemWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemWallet
        var returnedSystemWallet = om.readValue(
            restSystemWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemWallet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemWallet.class
        );

        // Validate the SystemWallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSystemWalletUpdatableFieldsEquals(returnedSystemWallet, getPersistedSystemWallet(returnedSystemWallet));

        insertedSystemWallet = returnedSystemWallet;
    }

    @Test
    @Transactional
    void createSystemWalletWithExistingId() throws Exception {
        // Create the SystemWallet with an existing ID
        systemWallet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemWallet)))
            .andExpect(status().isBadRequest());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSystemWallets() throws Exception {
        // Initialize the database
        insertedSystemWallet = systemWalletRepository.saveAndFlush(systemWallet);

        // Get all the systemWalletList
        restSystemWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].systemWalletID").value(hasItem(DEFAULT_SYSTEM_WALLET_ID.toString())))
            .andExpect(jsonPath("$.[*].before").value(hasItem(DEFAULT_BEFORE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT)))
            .andExpect(jsonPath("$.[*].blockAmount").value(hasItem(DEFAULT_BLOCK_AMOUNT)))
            .andExpect(jsonPath("$.[*].mobifyDate").value(hasItem(DEFAULT_MOBIFY_DATE.toString())));
    }

    @Test
    @Transactional
    void getSystemWallet() throws Exception {
        // Initialize the database
        insertedSystemWallet = systemWalletRepository.saveAndFlush(systemWallet);

        // Get the systemWallet
        restSystemWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, systemWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemWallet.getId().intValue()))
            .andExpect(jsonPath("$.systemWalletID").value(DEFAULT_SYSTEM_WALLET_ID.toString()))
            .andExpect(jsonPath("$.before").value(DEFAULT_BEFORE))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT))
            .andExpect(jsonPath("$.blockAmount").value(DEFAULT_BLOCK_AMOUNT))
            .andExpect(jsonPath("$.mobifyDate").value(DEFAULT_MOBIFY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemWallet() throws Exception {
        // Get the systemWallet
        restSystemWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemWallet() throws Exception {
        // Initialize the database
        insertedSystemWallet = systemWalletRepository.saveAndFlush(systemWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemWallet
        SystemWallet updatedSystemWallet = systemWalletRepository.findById(systemWallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemWallet are not directly saved in db
        em.detach(updatedSystemWallet);
        updatedSystemWallet
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .blockAmount(UPDATED_BLOCK_AMOUNT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restSystemWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSystemWallet))
            )
            .andExpect(status().isOk());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemWalletToMatchAllProperties(updatedSystemWallet);
    }

    @Test
    @Transactional
    void putNonExistingSystemWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemWallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemWalletWithPatch() throws Exception {
        // Initialize the database
        insertedSystemWallet = systemWalletRepository.saveAndFlush(systemWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemWallet using partial update
        SystemWallet partialUpdatedSystemWallet = new SystemWallet();
        partialUpdatedSystemWallet.setId(systemWallet.getId());

        partialUpdatedSystemWallet
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .current(UPDATED_CURRENT)
            .blockAmount(UPDATED_BLOCK_AMOUNT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restSystemWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemWallet))
            )
            .andExpect(status().isOk());

        // Validate the SystemWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemWalletUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemWallet, systemWallet),
            getPersistedSystemWallet(systemWallet)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemWalletWithPatch() throws Exception {
        // Initialize the database
        insertedSystemWallet = systemWalletRepository.saveAndFlush(systemWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemWallet using partial update
        SystemWallet partialUpdatedSystemWallet = new SystemWallet();
        partialUpdatedSystemWallet.setId(systemWallet.getId());

        partialUpdatedSystemWallet
            .systemWalletID(UPDATED_SYSTEM_WALLET_ID)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .blockAmount(UPDATED_BLOCK_AMOUNT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restSystemWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemWallet))
            )
            .andExpect(status().isOk());

        // Validate the SystemWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemWalletUpdatableFieldsEquals(partialUpdatedSystemWallet, getPersistedSystemWallet(partialUpdatedSystemWallet));
    }

    @Test
    @Transactional
    void patchNonExistingSystemWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemWallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemWallet() throws Exception {
        // Initialize the database
        insertedSystemWallet = systemWalletRepository.saveAndFlush(systemWallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemWallet
        restSystemWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemWalletRepository.count();
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

    protected SystemWallet getPersistedSystemWallet(SystemWallet systemWallet) {
        return systemWalletRepository.findById(systemWallet.getId()).orElseThrow();
    }

    protected void assertPersistedSystemWalletToMatchAllProperties(SystemWallet expectedSystemWallet) {
        assertSystemWalletAllPropertiesEquals(expectedSystemWallet, getPersistedSystemWallet(expectedSystemWallet));
    }

    protected void assertPersistedSystemWalletToMatchUpdatableProperties(SystemWallet expectedSystemWallet) {
        assertSystemWalletAllUpdatablePropertiesEquals(expectedSystemWallet, getPersistedSystemWallet(expectedSystemWallet));
    }
}
