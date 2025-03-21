package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.WalletTransactionAsserts.*;
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
import strip.domain.WalletTransaction;
import strip.domain.enumeration.TransactionStatus;
import strip.domain.enumeration.WalletTransactionType;
import strip.repository.WalletTransactionRepository;

/**
 * Integration tests for the {@link WalletTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WalletTransactionResourceIT {

    private static final UUID DEFAULT_TRANS_ID = UUID.randomUUID();
    private static final UUID UPDATED_TRANS_ID = UUID.randomUUID();

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final WalletTransactionType DEFAULT_WALLET_TYPE = WalletTransactionType.DEPOSIT;
    private static final WalletTransactionType UPDATED_WALLET_TYPE = WalletTransactionType.WITHDRAW;

    private static final TransactionStatus DEFAULT_TRANS_STATUS = TransactionStatus.PENDING;
    private static final TransactionStatus UPDATED_TRANS_STATUS = TransactionStatus.SUCCESS;

    private static final String DEFAULT_TRANSACTION_THIRD_PARTY_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_THIRD_PARTY_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/wallet-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalletTransactionMockMvc;

    private WalletTransaction walletTransaction;

    private WalletTransaction insertedWalletTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createEntity() {
        return new WalletTransaction()
            .transID(DEFAULT_TRANS_ID)
            .amount(DEFAULT_AMOUNT)
            .date(DEFAULT_DATE)
            .walletType(DEFAULT_WALLET_TYPE)
            .transStatus(DEFAULT_TRANS_STATUS)
            .transactionThirdPartyID(DEFAULT_TRANSACTION_THIRD_PARTY_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WalletTransaction createUpdatedEntity() {
        return new WalletTransaction()
            .transID(UPDATED_TRANS_ID)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .walletType(UPDATED_WALLET_TYPE)
            .transStatus(UPDATED_TRANS_STATUS)
            .transactionThirdPartyID(UPDATED_TRANSACTION_THIRD_PARTY_ID);
    }

    @BeforeEach
    public void initTest() {
        walletTransaction = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedWalletTransaction != null) {
            walletTransactionRepository.delete(insertedWalletTransaction);
            insertedWalletTransaction = null;
        }
    }

    @Test
    @Transactional
    void createWalletTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WalletTransaction
        var returnedWalletTransaction = om.readValue(
            restWalletTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransaction)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WalletTransaction.class
        );

        // Validate the WalletTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertWalletTransactionUpdatableFieldsEquals(returnedWalletTransaction, getPersistedWalletTransaction(returnedWalletTransaction));

        insertedWalletTransaction = returnedWalletTransaction;
    }

    @Test
    @Transactional
    void createWalletTransactionWithExistingId() throws Exception {
        // Create the WalletTransaction with an existing ID
        walletTransaction.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWalletTransactions() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get all the walletTransactionList
        restWalletTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transID").value(hasItem(DEFAULT_TRANS_ID.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].walletType").value(hasItem(DEFAULT_WALLET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].transStatus").value(hasItem(DEFAULT_TRANS_STATUS.toString())))
            .andExpect(jsonPath("$.[*].transactionThirdPartyID").value(hasItem(DEFAULT_TRANSACTION_THIRD_PARTY_ID)));
    }

    @Test
    @Transactional
    void getWalletTransaction() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get the walletTransaction
        restWalletTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, walletTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(walletTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transID").value(DEFAULT_TRANS_ID.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.walletType").value(DEFAULT_WALLET_TYPE.toString()))
            .andExpect(jsonPath("$.transStatus").value(DEFAULT_TRANS_STATUS.toString()))
            .andExpect(jsonPath("$.transactionThirdPartyID").value(DEFAULT_TRANSACTION_THIRD_PARTY_ID));
    }

    @Test
    @Transactional
    void getNonExistingWalletTransaction() throws Exception {
        // Get the walletTransaction
        restWalletTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWalletTransaction() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletTransaction
        WalletTransaction updatedWalletTransaction = walletTransactionRepository.findById(walletTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWalletTransaction are not directly saved in db
        em.detach(updatedWalletTransaction);
        updatedWalletTransaction
            .transID(UPDATED_TRANS_ID)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .walletType(UPDATED_WALLET_TYPE)
            .transStatus(UPDATED_TRANS_STATUS)
            .transactionThirdPartyID(UPDATED_TRANSACTION_THIRD_PARTY_ID);

        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWalletTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWalletTransactionToMatchAllProperties(updatedWalletTransaction);
    }

    @Test
    @Transactional
    void putNonExistingWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walletTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(walletTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(walletTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction.amount(UPDATED_AMOUNT);

        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWalletTransaction, walletTransaction),
            getPersistedWalletTransaction(walletTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateWalletTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the walletTransaction using partial update
        WalletTransaction partialUpdatedWalletTransaction = new WalletTransaction();
        partialUpdatedWalletTransaction.setId(walletTransaction.getId());

        partialUpdatedWalletTransaction
            .transID(UPDATED_TRANS_ID)
            .amount(UPDATED_AMOUNT)
            .date(UPDATED_DATE)
            .walletType(UPDATED_WALLET_TYPE)
            .transStatus(UPDATED_TRANS_STATUS)
            .transactionThirdPartyID(UPDATED_TRANSACTION_THIRD_PARTY_ID);

        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWalletTransaction))
            )
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWalletTransactionUpdatableFieldsEquals(
            partialUpdatedWalletTransaction,
            getPersistedWalletTransaction(partialUpdatedWalletTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, walletTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(walletTransaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWalletTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        walletTransaction.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(walletTransaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WalletTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWalletTransaction() throws Exception {
        // Initialize the database
        insertedWalletTransaction = walletTransactionRepository.saveAndFlush(walletTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the walletTransaction
        restWalletTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, walletTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return walletTransactionRepository.count();
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

    protected WalletTransaction getPersistedWalletTransaction(WalletTransaction walletTransaction) {
        return walletTransactionRepository.findById(walletTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedWalletTransactionToMatchAllProperties(WalletTransaction expectedWalletTransaction) {
        assertWalletTransactionAllPropertiesEquals(expectedWalletTransaction, getPersistedWalletTransaction(expectedWalletTransaction));
    }

    protected void assertPersistedWalletTransactionToMatchUpdatableProperties(WalletTransaction expectedWalletTransaction) {
        assertWalletTransactionAllUpdatablePropertiesEquals(
            expectedWalletTransaction,
            getPersistedWalletTransaction(expectedWalletTransaction)
        );
    }
}
