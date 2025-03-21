package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.UserDetailAsserts.*;
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
import strip.domain.UserDetail;
import strip.repository.UserDetailRepository;
import strip.repository.UserRepository;

/**
 * Integration tests for the {@link UserDetailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserDetailResourceIT {

    private static final UUID DEFAULT_APP_USER_DETAIL = UUID.randomUUID();
    private static final UUID UPDATED_APP_USER_DETAIL = UUID.randomUUID();

    private static final byte[] DEFAULT_USERIMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_USERIMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_USERIMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_USERIMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_DOB = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DOB = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserDetailMockMvc;

    private UserDetail userDetail;

    private UserDetail insertedUserDetail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetail createEntity() {
        return new UserDetail()
            .appUserDetail(DEFAULT_APP_USER_DETAIL)
            .userimage(DEFAULT_USERIMAGE)
            .userimageContentType(DEFAULT_USERIMAGE_CONTENT_TYPE)
            .phone(DEFAULT_PHONE)
            .gender(DEFAULT_GENDER)
            .address(DEFAULT_ADDRESS)
            .dob(DEFAULT_DOB);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDetail createUpdatedEntity() {
        return new UserDetail()
            .appUserDetail(UPDATED_APP_USER_DETAIL)
            .userimage(UPDATED_USERIMAGE)
            .userimageContentType(UPDATED_USERIMAGE_CONTENT_TYPE)
            .phone(UPDATED_PHONE)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .dob(UPDATED_DOB);
    }

    @BeforeEach
    public void initTest() {
        userDetail = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserDetail != null) {
            userDetailRepository.delete(insertedUserDetail);
            insertedUserDetail = null;
        }
    }

    @Test
    @Transactional
    void createUserDetail() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserDetail
        var returnedUserDetail = om.readValue(
            restUserDetailMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetail)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserDetail.class
        );

        // Validate the UserDetail in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserDetailUpdatableFieldsEquals(returnedUserDetail, getPersistedUserDetail(returnedUserDetail));

        insertedUserDetail = returnedUserDetail;
    }

    @Test
    @Transactional
    void createUserDetailWithExistingId() throws Exception {
        // Create the UserDetail with an existing ID
        userDetail.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserDetailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetail)))
            .andExpect(status().isBadRequest());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserDetails() throws Exception {
        // Initialize the database
        insertedUserDetail = userDetailRepository.saveAndFlush(userDetail);

        // Get all the userDetailList
        restUserDetailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userDetail.getId().intValue())))
            .andExpect(jsonPath("$.[*].appUserDetail").value(hasItem(DEFAULT_APP_USER_DETAIL.toString())))
            .andExpect(jsonPath("$.[*].userimageContentType").value(hasItem(DEFAULT_USERIMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].userimage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_USERIMAGE))))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }

    @Test
    @Transactional
    void getUserDetail() throws Exception {
        // Initialize the database
        insertedUserDetail = userDetailRepository.saveAndFlush(userDetail);

        // Get the userDetail
        restUserDetailMockMvc
            .perform(get(ENTITY_API_URL_ID, userDetail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userDetail.getId().intValue()))
            .andExpect(jsonPath("$.appUserDetail").value(DEFAULT_APP_USER_DETAIL.toString()))
            .andExpect(jsonPath("$.userimageContentType").value(DEFAULT_USERIMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.userimage").value(Base64.getEncoder().encodeToString(DEFAULT_USERIMAGE)))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserDetail() throws Exception {
        // Get the userDetail
        restUserDetailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserDetail() throws Exception {
        // Initialize the database
        insertedUserDetail = userDetailRepository.saveAndFlush(userDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDetail
        UserDetail updatedUserDetail = userDetailRepository.findById(userDetail.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserDetail are not directly saved in db
        em.detach(updatedUserDetail);
        updatedUserDetail
            .appUserDetail(UPDATED_APP_USER_DETAIL)
            .userimage(UPDATED_USERIMAGE)
            .userimageContentType(UPDATED_USERIMAGE_CONTENT_TYPE)
            .phone(UPDATED_PHONE)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .dob(UPDATED_DOB);

        restUserDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserDetail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserDetail))
            )
            .andExpect(status().isOk());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserDetailToMatchAllProperties(updatedUserDetail);
    }

    @Test
    @Transactional
    void putNonExistingUserDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetail.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userDetail.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userDetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserDetailWithPatch() throws Exception {
        // Initialize the database
        insertedUserDetail = userDetailRepository.saveAndFlush(userDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDetail using partial update
        UserDetail partialUpdatedUserDetail = new UserDetail();
        partialUpdatedUserDetail.setId(userDetail.getId());

        partialUpdatedUserDetail.dob(UPDATED_DOB);

        restUserDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDetail))
            )
            .andExpect(status().isOk());

        // Validate the UserDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDetailUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserDetail, userDetail),
            getPersistedUserDetail(userDetail)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserDetailWithPatch() throws Exception {
        // Initialize the database
        insertedUserDetail = userDetailRepository.saveAndFlush(userDetail);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userDetail using partial update
        UserDetail partialUpdatedUserDetail = new UserDetail();
        partialUpdatedUserDetail.setId(userDetail.getId());

        partialUpdatedUserDetail
            .appUserDetail(UPDATED_APP_USER_DETAIL)
            .userimage(UPDATED_USERIMAGE)
            .userimageContentType(UPDATED_USERIMAGE_CONTENT_TYPE)
            .phone(UPDATED_PHONE)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .dob(UPDATED_DOB);

        restUserDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserDetail))
            )
            .andExpect(status().isOk());

        // Validate the UserDetail in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserDetailUpdatableFieldsEquals(partialUpdatedUserDetail, getPersistedUserDetail(partialUpdatedUserDetail));
    }

    @Test
    @Transactional
    void patchNonExistingUserDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetail.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userDetail.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userDetail))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserDetail() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userDetail.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserDetailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userDetail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserDetail in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserDetail() throws Exception {
        // Initialize the database
        insertedUserDetail = userDetailRepository.saveAndFlush(userDetail);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userDetail
        restUserDetailMockMvc
            .perform(delete(ENTITY_API_URL_ID, userDetail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userDetailRepository.count();
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

    protected UserDetail getPersistedUserDetail(UserDetail userDetail) {
        return userDetailRepository.findById(userDetail.getId()).orElseThrow();
    }

    protected void assertPersistedUserDetailToMatchAllProperties(UserDetail expectedUserDetail) {
        assertUserDetailAllPropertiesEquals(expectedUserDetail, getPersistedUserDetail(expectedUserDetail));
    }

    protected void assertPersistedUserDetailToMatchUpdatableProperties(UserDetail expectedUserDetail) {
        assertUserDetailAllUpdatablePropertiesEquals(expectedUserDetail, getPersistedUserDetail(expectedUserDetail));
    }
}
