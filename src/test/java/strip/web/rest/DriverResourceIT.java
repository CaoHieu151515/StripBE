package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.DriverAsserts.*;
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
import strip.domain.Driver;
import strip.domain.enumeration.DriverStatus;
import strip.repository.DriverRepository;
import strip.repository.UserRepository;
import strip.service.dto.DriverDTO;
import strip.service.mapper.DriverMapper;

/**
 * Integration tests for the {@link DriverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DriverResourceIT {

    private static final UUID DEFAULT_DRIVER_ID = UUID.randomUUID();
    private static final UUID UPDATED_DRIVER_ID = UUID.randomUUID();

    private static final Boolean DEFAULT_USEDTO_DRIVER = false;
    private static final Boolean UPDATED_USEDTO_DRIVER = true;

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DriverStatus DEFAULT_DRIVER_STATUS = DriverStatus.ACTIVE;
    private static final DriverStatus UPDATED_DRIVER_STATUS = DriverStatus.BANNED;

    private static final Integer DEFAULT_DRIVER_POINT = 1;
    private static final Integer UPDATED_DRIVER_POINT = 2;

    private static final Instant DEFAULT_BANNED_DAY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BANNED_DAY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_DRIVER_LICENSE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DRIVER_LICENSE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DRIVER_LICENSE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DRIVER_LICENSE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IDENTITY_CARD_FACE_UP = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IDENTITY_CARD_FACE_UP = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IDENTITY_CARD_FACE_UP_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IDENTITY_CARD_FACE_UP_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IDENTITY_CARD_FACEDOWN = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IDENTITY_CARD_FACEDOWN = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDriverMockMvc;

    private Driver driver;

    private Driver insertedDriver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver()
            .driverID(DEFAULT_DRIVER_ID)
            .usedtoDriver(DEFAULT_USEDTO_DRIVER)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .driverStatus(DEFAULT_DRIVER_STATUS)
            .driverPoint(DEFAULT_DRIVER_POINT)
            .bannedDay(DEFAULT_BANNED_DAY)
            .driverLicense(DEFAULT_DRIVER_LICENSE)
            .driverLicenseContentType(DEFAULT_DRIVER_LICENSE_CONTENT_TYPE)
            .identityCardFaceUp(DEFAULT_IDENTITY_CARD_FACE_UP)
            .identityCardFaceUpContentType(DEFAULT_IDENTITY_CARD_FACE_UP_CONTENT_TYPE)
            .identityCardFacedown(DEFAULT_IDENTITY_CARD_FACEDOWN)
            .identityCardFacedownContentType(DEFAULT_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE);
        return driver;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createUpdatedEntity(EntityManager em) {
        Driver driver = new Driver()
            .driverID(UPDATED_DRIVER_ID)
            .usedtoDriver(UPDATED_USEDTO_DRIVER)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .driverStatus(UPDATED_DRIVER_STATUS)
            .driverPoint(UPDATED_DRIVER_POINT)
            .bannedDay(UPDATED_BANNED_DAY)
            .driverLicense(UPDATED_DRIVER_LICENSE)
            .driverLicenseContentType(UPDATED_DRIVER_LICENSE_CONTENT_TYPE)
            .identityCardFaceUp(UPDATED_IDENTITY_CARD_FACE_UP)
            .identityCardFaceUpContentType(UPDATED_IDENTITY_CARD_FACE_UP_CONTENT_TYPE)
            .identityCardFacedown(UPDATED_IDENTITY_CARD_FACEDOWN)
            .identityCardFacedownContentType(UPDATED_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE);
        return driver;
    }

    @BeforeEach
    public void initTest() {
        driver = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedDriver != null) {
            driverRepository.delete(insertedDriver);
            insertedDriver = null;
        }
    }

    @Test
    @Transactional
    void createDriver() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);
        var returnedDriverDTO = om.readValue(
            restDriverMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DriverDTO.class
        );

        // Validate the Driver in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDriver = driverMapper.toEntity(returnedDriverDTO);
        assertDriverUpdatableFieldsEquals(returnedDriver, getPersistedDriver(returnedDriver));

        insertedDriver = returnedDriver;
    }

    @Test
    @Transactional
    void createDriverWithExistingId() throws Exception {
        // Create the Driver with an existing ID
        driver.setId(1L);
        DriverDTO driverDTO = driverMapper.toDto(driver);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDrivers() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        // Get all the driverList
        restDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].driverID").value(hasItem(DEFAULT_DRIVER_ID.toString())))
            .andExpect(jsonPath("$.[*].usedtoDriver").value(hasItem(DEFAULT_USEDTO_DRIVER.booleanValue())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].driverStatus").value(hasItem(DEFAULT_DRIVER_STATUS.toString())))
            .andExpect(jsonPath("$.[*].driverPoint").value(hasItem(DEFAULT_DRIVER_POINT)))
            .andExpect(jsonPath("$.[*].bannedDay").value(hasItem(DEFAULT_BANNED_DAY.toString())))
            .andExpect(jsonPath("$.[*].driverLicenseContentType").value(hasItem(DEFAULT_DRIVER_LICENSE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].driverLicense").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DRIVER_LICENSE))))
            .andExpect(jsonPath("$.[*].identityCardFaceUpContentType").value(hasItem(DEFAULT_IDENTITY_CARD_FACE_UP_CONTENT_TYPE)))
            .andExpect(
                jsonPath("$.[*].identityCardFaceUp").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IDENTITY_CARD_FACE_UP)))
            )
            .andExpect(jsonPath("$.[*].identityCardFacedownContentType").value(hasItem(DEFAULT_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE)))
            .andExpect(
                jsonPath("$.[*].identityCardFacedown").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IDENTITY_CARD_FACEDOWN)))
            );
    }

    @Test
    @Transactional
    void getDriver() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        // Get the driver
        restDriverMockMvc
            .perform(get(ENTITY_API_URL_ID, driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(driver.getId().intValue()))
            .andExpect(jsonPath("$.driverID").value(DEFAULT_DRIVER_ID.toString()))
            .andExpect(jsonPath("$.usedtoDriver").value(DEFAULT_USEDTO_DRIVER.booleanValue()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.driverStatus").value(DEFAULT_DRIVER_STATUS.toString()))
            .andExpect(jsonPath("$.driverPoint").value(DEFAULT_DRIVER_POINT))
            .andExpect(jsonPath("$.bannedDay").value(DEFAULT_BANNED_DAY.toString()))
            .andExpect(jsonPath("$.driverLicenseContentType").value(DEFAULT_DRIVER_LICENSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.driverLicense").value(Base64.getEncoder().encodeToString(DEFAULT_DRIVER_LICENSE)))
            .andExpect(jsonPath("$.identityCardFaceUpContentType").value(DEFAULT_IDENTITY_CARD_FACE_UP_CONTENT_TYPE))
            .andExpect(jsonPath("$.identityCardFaceUp").value(Base64.getEncoder().encodeToString(DEFAULT_IDENTITY_CARD_FACE_UP)))
            .andExpect(jsonPath("$.identityCardFacedownContentType").value(DEFAULT_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE))
            .andExpect(jsonPath("$.identityCardFacedown").value(Base64.getEncoder().encodeToString(DEFAULT_IDENTITY_CARD_FACEDOWN)));
    }

    @Test
    @Transactional
    void getNonExistingDriver() throws Exception {
        // Get the driver
        restDriverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDriver() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDriver are not directly saved in db
        em.detach(updatedDriver);
        updatedDriver
            .driverID(UPDATED_DRIVER_ID)
            .usedtoDriver(UPDATED_USEDTO_DRIVER)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .driverStatus(UPDATED_DRIVER_STATUS)
            .driverPoint(UPDATED_DRIVER_POINT)
            .bannedDay(UPDATED_BANNED_DAY)
            .driverLicense(UPDATED_DRIVER_LICENSE)
            .driverLicenseContentType(UPDATED_DRIVER_LICENSE_CONTENT_TYPE)
            .identityCardFaceUp(UPDATED_IDENTITY_CARD_FACE_UP)
            .identityCardFaceUpContentType(UPDATED_IDENTITY_CARD_FACE_UP_CONTENT_TYPE)
            .identityCardFacedown(UPDATED_IDENTITY_CARD_FACEDOWN)
            .identityCardFacedownContentType(UPDATED_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE);
        DriverDTO driverDTO = driverMapper.toDto(updatedDriver);

        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDriverToMatchAllProperties(updatedDriver);
    }

    @Test
    @Transactional
    void putNonExistingDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, driverDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver.expirationDate(UPDATED_EXPIRATION_DATE).driverStatus(UPDATED_DRIVER_STATUS).driverPoint(UPDATED_DRIVER_POINT);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDriver, driver), getPersistedDriver(driver));
    }

    @Test
    @Transactional
    void fullUpdateDriverWithPatch() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the driver using partial update
        Driver partialUpdatedDriver = new Driver();
        partialUpdatedDriver.setId(driver.getId());

        partialUpdatedDriver
            .driverID(UPDATED_DRIVER_ID)
            .usedtoDriver(UPDATED_USEDTO_DRIVER)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .driverStatus(UPDATED_DRIVER_STATUS)
            .driverPoint(UPDATED_DRIVER_POINT)
            .bannedDay(UPDATED_BANNED_DAY)
            .driverLicense(UPDATED_DRIVER_LICENSE)
            .driverLicenseContentType(UPDATED_DRIVER_LICENSE_CONTENT_TYPE)
            .identityCardFaceUp(UPDATED_IDENTITY_CARD_FACE_UP)
            .identityCardFaceUpContentType(UPDATED_IDENTITY_CARD_FACE_UP_CONTENT_TYPE)
            .identityCardFacedown(UPDATED_IDENTITY_CARD_FACEDOWN)
            .identityCardFacedownContentType(UPDATED_IDENTITY_CARD_FACEDOWN_CONTENT_TYPE);

        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDriver))
            )
            .andExpect(status().isOk());

        // Validate the Driver in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDriverUpdatableFieldsEquals(partialUpdatedDriver, getPersistedDriver(partialUpdatedDriver));
    }

    @Test
    @Transactional
    void patchNonExistingDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, driverDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(driverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        driver.setId(longCount.incrementAndGet());

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDriverMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(driverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Driver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDriver() throws Exception {
        // Initialize the database
        insertedDriver = driverRepository.saveAndFlush(driver);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the driver
        restDriverMockMvc
            .perform(delete(ENTITY_API_URL_ID, driver.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return driverRepository.count();
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

    protected Driver getPersistedDriver(Driver driver) {
        return driverRepository.findById(driver.getId()).orElseThrow();
    }

    protected void assertPersistedDriverToMatchAllProperties(Driver expectedDriver) {
        assertDriverAllPropertiesEquals(expectedDriver, getPersistedDriver(expectedDriver));
    }

    protected void assertPersistedDriverToMatchUpdatableProperties(Driver expectedDriver) {
        assertDriverAllUpdatablePropertiesEquals(expectedDriver, getPersistedDriver(expectedDriver));
    }
}
