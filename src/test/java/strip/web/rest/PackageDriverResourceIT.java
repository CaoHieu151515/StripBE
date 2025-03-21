package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.PackageDriverAsserts.*;
import static strip.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import strip.domain.PackageDriver;
import strip.domain.enumeration.PackageDriverStatus;
import strip.repository.PackageDriverRepository;
import strip.service.dto.PackageDriverDTO;
import strip.service.mapper.PackageDriverMapper;

/**
 * Integration tests for the {@link PackageDriverResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackageDriverResourceIT {

    private static final UUID DEFAULT_PACKAGE_ID = UUID.randomUUID();
    private static final UUID UPDATED_PACKAGE_ID = UUID.randomUUID();

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    private static final Integer DEFAULT_BONUS = 1;
    private static final Integer UPDATED_BONUS = 2;

    private static final PackageDriverStatus DEFAULT_STATUS = PackageDriverStatus.ACTIVE;
    private static final PackageDriverStatus UPDATED_STATUS = PackageDriverStatus.EXPIRED;

    private static final String ENTITY_API_URL = "/api/package-drivers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PackageDriverRepository packageDriverRepository;

    @Autowired
    private PackageDriverMapper packageDriverMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackageDriverMockMvc;

    private PackageDriver packageDriver;

    private PackageDriver insertedPackageDriver;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageDriver createEntity() {
        return new PackageDriver()
            .packageID(DEFAULT_PACKAGE_ID)
            .price(DEFAULT_PRICE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .time(DEFAULT_TIME)
            .bonus(DEFAULT_BONUS)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageDriver createUpdatedEntity() {
        return new PackageDriver()
            .packageID(UPDATED_PACKAGE_ID)
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .bonus(UPDATED_BONUS)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        packageDriver = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPackageDriver != null) {
            packageDriverRepository.delete(insertedPackageDriver);
            insertedPackageDriver = null;
        }
    }

    @Test
    @Transactional
    void createPackageDriver() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);
        var returnedPackageDriverDTO = om.readValue(
            restPackageDriverMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(packageDriverDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PackageDriverDTO.class
        );

        // Validate the PackageDriver in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPackageDriver = packageDriverMapper.toEntity(returnedPackageDriverDTO);
        assertPackageDriverUpdatableFieldsEquals(returnedPackageDriver, getPersistedPackageDriver(returnedPackageDriver));

        insertedPackageDriver = returnedPackageDriver;
    }

    @Test
    @Transactional
    void createPackageDriverWithExistingId() throws Exception {
        // Create the PackageDriver with an existing ID
        packageDriver.setId(1L);
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageDriverMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(packageDriverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPackageDrivers() throws Exception {
        // Initialize the database
        insertedPackageDriver = packageDriverRepository.saveAndFlush(packageDriver);

        // Get all the packageDriverList
        restPackageDriverMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageDriver.getId().intValue())))
            .andExpect(jsonPath("$.[*].packageID").value(hasItem(DEFAULT_PACKAGE_ID.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
            .andExpect(jsonPath("$.[*].bonus").value(hasItem(DEFAULT_BONUS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPackageDriver() throws Exception {
        // Initialize the database
        insertedPackageDriver = packageDriverRepository.saveAndFlush(packageDriver);

        // Get the packageDriver
        restPackageDriverMockMvc
            .perform(get(ENTITY_API_URL_ID, packageDriver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packageDriver.getId().intValue()))
            .andExpect(jsonPath("$.packageID").value(DEFAULT_PACKAGE_ID.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.bonus").value(DEFAULT_BONUS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPackageDriver() throws Exception {
        // Get the packageDriver
        restPackageDriverMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPackageDriver() throws Exception {
        // Initialize the database
        insertedPackageDriver = packageDriverRepository.saveAndFlush(packageDriver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the packageDriver
        PackageDriver updatedPackageDriver = packageDriverRepository.findById(packageDriver.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPackageDriver are not directly saved in db
        em.detach(updatedPackageDriver);
        updatedPackageDriver
            .packageID(UPDATED_PACKAGE_ID)
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .bonus(UPDATED_BONUS)
            .status(UPDATED_STATUS);
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(updatedPackageDriver);

        restPackageDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packageDriverDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(packageDriverDTO))
            )
            .andExpect(status().isOk());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPackageDriverToMatchAllProperties(updatedPackageDriver);
    }

    @Test
    @Transactional
    void putNonExistingPackageDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        packageDriver.setId(longCount.incrementAndGet());

        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packageDriverDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(packageDriverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPackageDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        packageDriver.setId(longCount.incrementAndGet());

        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageDriverMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(packageDriverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPackageDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        packageDriver.setId(longCount.incrementAndGet());

        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageDriverMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(packageDriverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackageDriverWithPatch() throws Exception {
        // Initialize the database
        insertedPackageDriver = packageDriverRepository.saveAndFlush(packageDriver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the packageDriver using partial update
        PackageDriver partialUpdatedPackageDriver = new PackageDriver();
        partialUpdatedPackageDriver.setId(packageDriver.getId());

        partialUpdatedPackageDriver
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .status(UPDATED_STATUS);

        restPackageDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackageDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPackageDriver))
            )
            .andExpect(status().isOk());

        // Validate the PackageDriver in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPackageDriverUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPackageDriver, packageDriver),
            getPersistedPackageDriver(packageDriver)
        );
    }

    @Test
    @Transactional
    void fullUpdatePackageDriverWithPatch() throws Exception {
        // Initialize the database
        insertedPackageDriver = packageDriverRepository.saveAndFlush(packageDriver);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the packageDriver using partial update
        PackageDriver partialUpdatedPackageDriver = new PackageDriver();
        partialUpdatedPackageDriver.setId(packageDriver.getId());

        partialUpdatedPackageDriver
            .packageID(UPDATED_PACKAGE_ID)
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .time(UPDATED_TIME)
            .bonus(UPDATED_BONUS)
            .status(UPDATED_STATUS);

        restPackageDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackageDriver.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPackageDriver))
            )
            .andExpect(status().isOk());

        // Validate the PackageDriver in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPackageDriverUpdatableFieldsEquals(partialUpdatedPackageDriver, getPersistedPackageDriver(partialUpdatedPackageDriver));
    }

    @Test
    @Transactional
    void patchNonExistingPackageDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        packageDriver.setId(longCount.incrementAndGet());

        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packageDriverDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(packageDriverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPackageDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        packageDriver.setId(longCount.incrementAndGet());

        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageDriverMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(packageDriverDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPackageDriver() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        packageDriver.setId(longCount.incrementAndGet());

        // Create the PackageDriver
        PackageDriverDTO packageDriverDTO = packageDriverMapper.toDto(packageDriver);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageDriverMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(packageDriverDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PackageDriver in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePackageDriver() throws Exception {
        // Initialize the database
        insertedPackageDriver = packageDriverRepository.saveAndFlush(packageDriver);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the packageDriver
        restPackageDriverMockMvc
            .perform(delete(ENTITY_API_URL_ID, packageDriver.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return packageDriverRepository.count();
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

    protected PackageDriver getPersistedPackageDriver(PackageDriver packageDriver) {
        return packageDriverRepository.findById(packageDriver.getId()).orElseThrow();
    }

    protected void assertPersistedPackageDriverToMatchAllProperties(PackageDriver expectedPackageDriver) {
        assertPackageDriverAllPropertiesEquals(expectedPackageDriver, getPersistedPackageDriver(expectedPackageDriver));
    }

    protected void assertPersistedPackageDriverToMatchUpdatableProperties(PackageDriver expectedPackageDriver) {
        assertPackageDriverAllUpdatablePropertiesEquals(expectedPackageDriver, getPersistedPackageDriver(expectedPackageDriver));
    }
}
