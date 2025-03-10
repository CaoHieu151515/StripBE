package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.SendingAplicationAsserts.*;
import static strip.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import strip.domain.SendingAplication;
import strip.domain.enumeration.AplicationType;
import strip.repository.SendingAplicationRepository;
import strip.repository.UserRepository;

/**
 * Integration tests for the {@link SendingAplicationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SendingAplicationResourceIT {

    private static final UUID DEFAULT_APLI_ID = UUID.randomUUID();
    private static final UUID UPDATED_APLI_ID = UUID.randomUUID();

    private static final AplicationType DEFAULT_SEND_APPLICATION_TYPE = AplicationType.FEEDBACKSYSTEM;
    private static final AplicationType UPDATED_SEND_APPLICATION_TYPE = AplicationType.REPORTBUG;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMG_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/sending-aplications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SendingAplicationRepository sendingAplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSendingAplicationMockMvc;

    private SendingAplication sendingAplication;

    private SendingAplication insertedSendingAplication;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SendingAplication createEntity(EntityManager em) {
        SendingAplication sendingAplication = new SendingAplication()
            .apliID(DEFAULT_APLI_ID)
            .sendApplicationType(DEFAULT_SEND_APPLICATION_TYPE)
            .content(DEFAULT_CONTENT)
            .img(DEFAULT_IMG)
            .imgContentType(DEFAULT_IMG_CONTENT_TYPE);
        return sendingAplication;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SendingAplication createUpdatedEntity(EntityManager em) {
        SendingAplication sendingAplication = new SendingAplication()
            .apliID(UPDATED_APLI_ID)
            .sendApplicationType(UPDATED_SEND_APPLICATION_TYPE)
            .content(UPDATED_CONTENT)
            .img(UPDATED_IMG)
            .imgContentType(UPDATED_IMG_CONTENT_TYPE);
        return sendingAplication;
    }

    @BeforeEach
    public void initTest() {
        sendingAplication = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSendingAplication != null) {
            sendingAplicationRepository.delete(insertedSendingAplication);
            insertedSendingAplication = null;
        }
    }

    @Test
    @Transactional
    void createSendingAplication() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SendingAplication
        var returnedSendingAplication = om.readValue(
            restSendingAplicationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendingAplication)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SendingAplication.class
        );

        // Validate the SendingAplication in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSendingAplicationUpdatableFieldsEquals(returnedSendingAplication, getPersistedSendingAplication(returnedSendingAplication));

        insertedSendingAplication = returnedSendingAplication;
    }

    @Test
    @Transactional
    void createSendingAplicationWithExistingId() throws Exception {
        // Create the SendingAplication with an existing ID
        sendingAplication.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSendingAplicationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendingAplication)))
            .andExpect(status().isBadRequest());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSendingAplications() throws Exception {
        // Initialize the database
        insertedSendingAplication = sendingAplicationRepository.saveAndFlush(sendingAplication);

        // Get all the sendingAplicationList
        restSendingAplicationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sendingAplication.getId().intValue())))
            .andExpect(jsonPath("$.[*].apliID").value(hasItem(DEFAULT_APLI_ID.toString())))
            .andExpect(jsonPath("$.[*].sendApplicationType").value(hasItem(DEFAULT_SEND_APPLICATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].imgContentType").value(hasItem(DEFAULT_IMG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMG))));
    }

    @Test
    @Transactional
    void getSendingAplication() throws Exception {
        // Initialize the database
        insertedSendingAplication = sendingAplicationRepository.saveAndFlush(sendingAplication);

        // Get the sendingAplication
        restSendingAplicationMockMvc
            .perform(get(ENTITY_API_URL_ID, sendingAplication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sendingAplication.getId().intValue()))
            .andExpect(jsonPath("$.apliID").value(DEFAULT_APLI_ID.toString()))
            .andExpect(jsonPath("$.sendApplicationType").value(DEFAULT_SEND_APPLICATION_TYPE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.imgContentType").value(DEFAULT_IMG_CONTENT_TYPE))
            .andExpect(jsonPath("$.img").value(Base64.getEncoder().encodeToString(DEFAULT_IMG)));
    }

    @Test
    @Transactional
    void getNonExistingSendingAplication() throws Exception {
        // Get the sendingAplication
        restSendingAplicationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSendingAplication() throws Exception {
        // Initialize the database
        insertedSendingAplication = sendingAplicationRepository.saveAndFlush(sendingAplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sendingAplication
        SendingAplication updatedSendingAplication = sendingAplicationRepository.findById(sendingAplication.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSendingAplication are not directly saved in db
        em.detach(updatedSendingAplication);
        updatedSendingAplication
            .apliID(UPDATED_APLI_ID)
            .sendApplicationType(UPDATED_SEND_APPLICATION_TYPE)
            .content(UPDATED_CONTENT)
            .img(UPDATED_IMG)
            .imgContentType(UPDATED_IMG_CONTENT_TYPE);

        restSendingAplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSendingAplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSendingAplication))
            )
            .andExpect(status().isOk());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSendingAplicationToMatchAllProperties(updatedSendingAplication);
    }

    @Test
    @Transactional
    void putNonExistingSendingAplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendingAplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSendingAplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sendingAplication.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sendingAplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSendingAplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendingAplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendingAplicationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(sendingAplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSendingAplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendingAplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendingAplicationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(sendingAplication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSendingAplicationWithPatch() throws Exception {
        // Initialize the database
        insertedSendingAplication = sendingAplicationRepository.saveAndFlush(sendingAplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sendingAplication using partial update
        SendingAplication partialUpdatedSendingAplication = new SendingAplication();
        partialUpdatedSendingAplication.setId(sendingAplication.getId());

        partialUpdatedSendingAplication.sendApplicationType(UPDATED_SEND_APPLICATION_TYPE);

        restSendingAplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSendingAplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSendingAplication))
            )
            .andExpect(status().isOk());

        // Validate the SendingAplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSendingAplicationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSendingAplication, sendingAplication),
            getPersistedSendingAplication(sendingAplication)
        );
    }

    @Test
    @Transactional
    void fullUpdateSendingAplicationWithPatch() throws Exception {
        // Initialize the database
        insertedSendingAplication = sendingAplicationRepository.saveAndFlush(sendingAplication);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the sendingAplication using partial update
        SendingAplication partialUpdatedSendingAplication = new SendingAplication();
        partialUpdatedSendingAplication.setId(sendingAplication.getId());

        partialUpdatedSendingAplication
            .apliID(UPDATED_APLI_ID)
            .sendApplicationType(UPDATED_SEND_APPLICATION_TYPE)
            .content(UPDATED_CONTENT)
            .img(UPDATED_IMG)
            .imgContentType(UPDATED_IMG_CONTENT_TYPE);

        restSendingAplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSendingAplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSendingAplication))
            )
            .andExpect(status().isOk());

        // Validate the SendingAplication in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSendingAplicationUpdatableFieldsEquals(
            partialUpdatedSendingAplication,
            getPersistedSendingAplication(partialUpdatedSendingAplication)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSendingAplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendingAplication.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSendingAplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sendingAplication.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sendingAplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSendingAplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendingAplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendingAplicationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(sendingAplication))
            )
            .andExpect(status().isBadRequest());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSendingAplication() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        sendingAplication.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSendingAplicationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(sendingAplication)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SendingAplication in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSendingAplication() throws Exception {
        // Initialize the database
        insertedSendingAplication = sendingAplicationRepository.saveAndFlush(sendingAplication);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the sendingAplication
        restSendingAplicationMockMvc
            .perform(delete(ENTITY_API_URL_ID, sendingAplication.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return sendingAplicationRepository.count();
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

    protected SendingAplication getPersistedSendingAplication(SendingAplication sendingAplication) {
        return sendingAplicationRepository.findById(sendingAplication.getId()).orElseThrow();
    }

    protected void assertPersistedSendingAplicationToMatchAllProperties(SendingAplication expectedSendingAplication) {
        assertSendingAplicationAllPropertiesEquals(expectedSendingAplication, getPersistedSendingAplication(expectedSendingAplication));
    }

    protected void assertPersistedSendingAplicationToMatchUpdatableProperties(SendingAplication expectedSendingAplication) {
        assertSendingAplicationAllUpdatablePropertiesEquals(
            expectedSendingAplication,
            getPersistedSendingAplication(expectedSendingAplication)
        );
    }
}
