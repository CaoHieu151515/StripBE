package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.UserWalletAsserts.*;
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
import strip.domain.UserWallet;
import strip.repository.UserRepository;
import strip.repository.UserWalletRepository;

/**
 * Integration tests for the {@link UserWalletResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserWalletResourceIT {

    private static final UUID DEFAULT_USER_WALLET = UUID.randomUUID();
    private static final UUID UPDATED_USER_WALLET = UUID.randomUUID();

    private static final Double DEFAULT_BEFORE = 1D;
    private static final Double UPDATED_BEFORE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Double DEFAULT_CURRENT = 1D;
    private static final Double UPDATED_CURRENT = 2D;

    private static final Instant DEFAULT_MOBIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MOBIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserWalletRepository userWalletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserWalletMockMvc;

    private UserWallet userWallet;

    private UserWallet insertedUserWallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserWallet createEntity() {
        return new UserWallet()
            .userWallet(DEFAULT_USER_WALLET)
            .before(DEFAULT_BEFORE)
            .amount(DEFAULT_AMOUNT)
            .current(DEFAULT_CURRENT)
            .mobifyDate(DEFAULT_MOBIFY_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserWallet createUpdatedEntity() {
        return new UserWallet()
            .userWallet(UPDATED_USER_WALLET)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);
    }

    @BeforeEach
    public void initTest() {
        userWallet = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserWallet != null) {
            userWalletRepository.delete(insertedUserWallet);
            insertedUserWallet = null;
        }
    }

    @Test
    @Transactional
    void createUserWallet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserWallet
        var returnedUserWallet = om.readValue(
            restUserWalletMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userWallet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserWallet.class
        );

        // Validate the UserWallet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserWalletUpdatableFieldsEquals(returnedUserWallet, getPersistedUserWallet(returnedUserWallet));

        insertedUserWallet = returnedUserWallet;
    }

    @Test
    @Transactional
    void createUserWalletWithExistingId() throws Exception {
        // Create the UserWallet with an existing ID
        userWallet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userWallet)))
            .andExpect(status().isBadRequest());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserWallets() throws Exception {
        // Initialize the database
        insertedUserWallet = userWalletRepository.saveAndFlush(userWallet);

        // Get all the userWalletList
        restUserWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userWallet.getId().intValue())))
            .andExpect(jsonPath("$.[*].userWallet").value(hasItem(DEFAULT_USER_WALLET.toString())))
            .andExpect(jsonPath("$.[*].before").value(hasItem(DEFAULT_BEFORE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT)))
            .andExpect(jsonPath("$.[*].mobifyDate").value(hasItem(DEFAULT_MOBIFY_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserWallet() throws Exception {
        // Initialize the database
        insertedUserWallet = userWalletRepository.saveAndFlush(userWallet);

        // Get the userWallet
        restUserWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, userWallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userWallet.getId().intValue()))
            .andExpect(jsonPath("$.userWallet").value(DEFAULT_USER_WALLET.toString()))
            .andExpect(jsonPath("$.before").value(DEFAULT_BEFORE))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT))
            .andExpect(jsonPath("$.mobifyDate").value(DEFAULT_MOBIFY_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserWallet() throws Exception {
        // Get the userWallet
        restUserWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserWallet() throws Exception {
        // Initialize the database
        insertedUserWallet = userWalletRepository.saveAndFlush(userWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userWallet
        UserWallet updatedUserWallet = userWalletRepository.findById(userWallet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserWallet are not directly saved in db
        em.detach(updatedUserWallet);
        updatedUserWallet
            .userWallet(UPDATED_USER_WALLET)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restUserWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserWallet))
            )
            .andExpect(status().isOk());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserWalletToMatchAllProperties(updatedUserWallet);
    }

    @Test
    @Transactional
    void putNonExistingUserWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userWallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userWallet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserWalletWithPatch() throws Exception {
        // Initialize the database
        insertedUserWallet = userWalletRepository.saveAndFlush(userWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userWallet using partial update
        UserWallet partialUpdatedUserWallet = new UserWallet();
        partialUpdatedUserWallet.setId(userWallet.getId());

        partialUpdatedUserWallet
            .userWallet(UPDATED_USER_WALLET)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restUserWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserWallet))
            )
            .andExpect(status().isOk());

        // Validate the UserWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserWalletUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserWallet, userWallet),
            getPersistedUserWallet(userWallet)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserWalletWithPatch() throws Exception {
        // Initialize the database
        insertedUserWallet = userWalletRepository.saveAndFlush(userWallet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userWallet using partial update
        UserWallet partialUpdatedUserWallet = new UserWallet();
        partialUpdatedUserWallet.setId(userWallet.getId());

        partialUpdatedUserWallet
            .userWallet(UPDATED_USER_WALLET)
            .before(UPDATED_BEFORE)
            .amount(UPDATED_AMOUNT)
            .current(UPDATED_CURRENT)
            .mobifyDate(UPDATED_MOBIFY_DATE);

        restUserWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserWallet))
            )
            .andExpect(status().isOk());

        // Validate the UserWallet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserWalletUpdatableFieldsEquals(partialUpdatedUserWallet, getPersistedUserWallet(partialUpdatedUserWallet));
    }

    @Test
    @Transactional
    void patchNonExistingUserWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userWallet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userWallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserWallet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userWallet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userWallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserWallet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserWallet() throws Exception {
        // Initialize the database
        insertedUserWallet = userWalletRepository.saveAndFlush(userWallet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userWallet
        restUserWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, userWallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userWalletRepository.count();
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

    protected UserWallet getPersistedUserWallet(UserWallet userWallet) {
        return userWalletRepository.findById(userWallet.getId()).orElseThrow();
    }

    protected void assertPersistedUserWalletToMatchAllProperties(UserWallet expectedUserWallet) {
        assertUserWalletAllPropertiesEquals(expectedUserWallet, getPersistedUserWallet(expectedUserWallet));
    }

    protected void assertPersistedUserWalletToMatchUpdatableProperties(UserWallet expectedUserWallet) {
        assertUserWalletAllUpdatablePropertiesEquals(expectedUserWallet, getPersistedUserWallet(expectedUserWallet));
    }
}
