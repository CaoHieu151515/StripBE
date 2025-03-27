package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.WalletDepositAsserts.*;
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
import strip.domain.WalletDeposit;
import strip.domain.enumeration.PaymentStatus;
import strip.repository.WalletDepositRepository;

/**
 * Integration tests for the {@link WalletDepositResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WalletDepositResourceIT {

    private static final String DEFAULT_BANK_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_OF_BANK = "AAAAAAAAAA";
    private static final String UPDATED_NAME_OF_BANK = "BBBBBBBBBB";

    private static final String DEFAULT_BANK = "AAAAAAAAAA";
    private static final String UPDATED_BANK = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final PaymentStatus DEFAULT_STATUS = PaymentStatus.PENDING;
    private static final PaymentStatus UPDATED_STATUS = PaymentStatus.SUCCESS;

    private static final String ENTITY_API_URL = "/api/wallet-deposits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WalletDepositRepository walletDepositRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletDepositMockMvc;

    private WalletDeposit walletDeposit;

    private WalletDeposit insertedWalletDeposit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletDeposit createEntity() {
        return new WalletDeposit()
            .bankNumber(DEFAULT_BANK_NUMBER)
            .nameOfBank(DEFAULT_NAME_OF_BANK)
            .bank(DEFAULT_BANK)
            .amount(DEFAULT_AMOUNT)
            .date(DEFAULT_DATE)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletDeposit createUpdatedEntity() {
        return new WalletDeposit()
            .bankNumber(UPDATED_BANK_NUMBER)
            .nameOfBank(UPDATED_NAME_OF_BANK)
            .bank(UPDATED_BANK)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        walletDeposit = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWalletDeposit != null) {
            walletDepositRepository.delete(insertedWalletDeposit);
            insertedWalletDeposit = null;
        }
    }

    @Test
    @Transactional
    void createWalletDeposit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WalletDeposit
        var returnedWalletDeposit = om.readValue(
            restWalletDepositMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletDeposit)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WalletDeposit.class
        );

        // Validate the WalletDeposit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWalletDepositUpdatableFieldsEquals(returnedWalletDeposit, getPersistedWalletDeposit(returnedWalletDeposit));

        insertedWalletDeposit = returnedWalletDeposit;
    }

    @Test
    @Transactional
    void createWalletDepositWithExistingId() throws Exception {
        // Create the WalletDeposit with an existing ID
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletDepositMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletDeposit)))
            .andExpect(status().isBadRequest());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWalletDeposits() throws Exception {
        // Initialize the database
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        // Get all the walletDepositList
        restWalletDepositMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletDeposit.getId().toString())))
            .andExpect(jsonPath("$.[*].bankNumber").value(hasItem(DEFAULT_BANK_NUMBER)))
            .andExpect(jsonPath("$.[*].nameOfBank").value(hasItem(DEFAULT_NAME_OF_BANK)))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getWalletDeposit() throws Exception {
        // Initialize the database
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        // Get the walletDeposit
        restWalletDepositMockMvc
            .perform(get(ENTITY_API_URL_ID, walletDeposit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(walletDeposit.getId().toString()))
            .andExpect(jsonPath("$.bankNumber").value(DEFAULT_BANK_NUMBER))
            .andExpect(jsonPath("$.nameOfBank").value(DEFAULT_NAME_OF_BANK))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWalletDeposit() throws Exception {
        // Get the walletDeposit
        restWalletDepositMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWalletDeposit() throws Exception {
        // Initialize the database
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletDeposit
        WalletDeposit updatedWalletDeposit = walletDepositRepository.findById(walletDeposit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWalletDeposit are not directly saved in db
        em.detach(updatedWalletDeposit);
        updatedWalletDeposit
            .bankNumber(UPDATED_BANK_NUMBER)
            .nameOfBank(UPDATED_NAME_OF_BANK)
            .bank(UPDATED_BANK)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restWalletDepositMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWalletDeposit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWalletDeposit))
            )
            .andExpect(status().isOk());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWalletDepositToMatchAllProperties(updatedWalletDeposit);
    }

    @Test
    @Transactional
    void putNonExistingWalletDeposit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletDeposit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletDepositMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletDeposit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletDeposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWalletDeposit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletDeposit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletDepositMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletDeposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWalletDeposit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletDeposit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletDepositMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletDeposit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletDepositWithPatch() throws Exception {
        // Initialize the database
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletDeposit using partial update
        WalletDeposit partialUpdatedWalletDeposit = new WalletDeposit();
        partialUpdatedWalletDeposit.setId(walletDeposit.getId());

        partialUpdatedWalletDeposit.bankNumber(UPDATED_BANK_NUMBER).amount(UPDATED_AMOUNT).date(UPDATED_DATE);

        restWalletDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletDeposit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletDeposit))
            )
            .andExpect(status().isOk());

        // Validate the WalletDeposit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletDepositUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWalletDeposit, walletDeposit),
            getPersistedWalletDeposit(walletDeposit)
        );
    }

    @Test
    @Transactional
    void fullUpdateWalletDepositWithPatch() throws Exception {
        // Initialize the database
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletDeposit using partial update
        WalletDeposit partialUpdatedWalletDeposit = new WalletDeposit();
        partialUpdatedWalletDeposit.setId(walletDeposit.getId());

        partialUpdatedWalletDeposit
            .bankNumber(UPDATED_BANK_NUMBER)
            .nameOfBank(UPDATED_NAME_OF_BANK)
            .bank(UPDATED_BANK)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .status(UPDATED_STATUS);

        restWalletDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletDeposit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletDeposit))
            )
            .andExpect(status().isOk());

        // Validate the WalletDeposit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletDepositUpdatableFieldsEquals(partialUpdatedWalletDeposit, getPersistedWalletDeposit(partialUpdatedWalletDeposit));
    }

    @Test
    @Transactional
    void patchNonExistingWalletDeposit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletDeposit.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, walletDeposit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletDeposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWalletDeposit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletDeposit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletDepositMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletDeposit))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWalletDeposit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletDeposit.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletDepositMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(walletDeposit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletDeposit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWalletDeposit() throws Exception {
        // Initialize the database
        insertedWalletDeposit = walletDepositRepository.saveAndFlush(walletDeposit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the walletDeposit
        restWalletDepositMockMvc
            .perform(delete(ENTITY_API_URL_ID, walletDeposit.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return walletDepositRepository.count();
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

    protected WalletDeposit getPersistedWalletDeposit(WalletDeposit walletDeposit) {
        return walletDepositRepository.findById(walletDeposit.getId()).orElseThrow();
    }

    protected void assertPersistedWalletDepositToMatchAllProperties(WalletDeposit expectedWalletDeposit) {
        assertWalletDepositAllPropertiesEquals(expectedWalletDeposit, getPersistedWalletDeposit(expectedWalletDeposit));
    }

    protected void assertPersistedWalletDepositToMatchUpdatableProperties(WalletDeposit expectedWalletDeposit) {
        assertWalletDepositAllUpdatablePropertiesEquals(expectedWalletDeposit, getPersistedWalletDeposit(expectedWalletDeposit));
    }
}
