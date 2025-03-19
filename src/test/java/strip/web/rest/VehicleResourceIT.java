package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.VehicleAsserts.*;
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
import strip.domain.Vehicle;
import strip.domain.enumeration.VehicleStatus;
import strip.domain.enumeration.VehicleType;
import strip.repository.VehicleRepository;
import strip.service.dto.VehicleDTO;
import strip.service.mapper.VehicleMapper;

/**
 * Integration tests for the {@link VehicleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleResourceIT {

    private static final UUID DEFAULT_VEHICLE_ID = UUID.randomUUID();
    private static final UUID UPDATED_VEHICLE_ID = UUID.randomUUID();

    private static final VehicleType DEFAULT_VEHICLE_TYPE = VehicleType.CAR;
    private static final VehicleType UPDATED_VEHICLE_TYPE = VehicleType.BIKE;

    private static final byte[] DEFAULT_VEHICLE_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VEHICLE_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VEHICLE_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VEHICLE_IMAGE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_CARREGISTRATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CARREGISTRATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CARREGISTRATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CARREGISTRATION_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_VEHICLE_INSPECTION_CERTIFICATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VEHICLE_INSPECTION_CERTIFICATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_CAR_INSURANCE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CAR_INSURANCE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CAR_INSURANCE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CAR_INSURANCE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_VEHICLE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_NUMBER = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_SEATS = 1;
    private static final Integer UPDATED_NUMBER_OF_SEATS = 2;

    private static final String DEFAULT_VEHICLE_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_VEHICLE_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_BRAND = "BBBBBBBBBB";

    private static final VehicleStatus DEFAULT_STATUS = VehicleStatus.ACTIVE;
    private static final VehicleStatus UPDATED_STATUS = VehicleStatus.CONFIRMING;

    private static final String ENTITY_API_URL = "/api/vehicles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleMockMvc;

    private Vehicle vehicle;

    private Vehicle insertedVehicle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createEntity() {
        return new Vehicle()
            .vehicleID(DEFAULT_VEHICLE_ID)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .vehicleImage(DEFAULT_VEHICLE_IMAGE)
            .vehicleImageContentType(DEFAULT_VEHICLE_IMAGE_CONTENT_TYPE)
            .carregistration(DEFAULT_CARREGISTRATION)
            .carregistrationContentType(DEFAULT_CARREGISTRATION_CONTENT_TYPE)
            .vehicleInspectionCertificate(DEFAULT_VEHICLE_INSPECTION_CERTIFICATE)
            .vehicleInspectionCertificateContentType(DEFAULT_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE)
            .carInsurance(DEFAULT_CAR_INSURANCE)
            .carInsuranceContentType(DEFAULT_CAR_INSURANCE_CONTENT_TYPE)
            .vehicleNumber(DEFAULT_VEHICLE_NUMBER)
            .numberOfSeats(DEFAULT_NUMBER_OF_SEATS)
            .vehicleColor(DEFAULT_VEHICLE_COLOR)
            .vehicleBrand(DEFAULT_VEHICLE_BRAND)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicle createUpdatedEntity() {
        return new Vehicle()
            .vehicleID(UPDATED_VEHICLE_ID)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleImage(UPDATED_VEHICLE_IMAGE)
            .vehicleImageContentType(UPDATED_VEHICLE_IMAGE_CONTENT_TYPE)
            .carregistration(UPDATED_CARREGISTRATION)
            .carregistrationContentType(UPDATED_CARREGISTRATION_CONTENT_TYPE)
            .vehicleInspectionCertificate(UPDATED_VEHICLE_INSPECTION_CERTIFICATE)
            .vehicleInspectionCertificateContentType(UPDATED_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE)
            .carInsurance(UPDATED_CAR_INSURANCE)
            .carInsuranceContentType(UPDATED_CAR_INSURANCE_CONTENT_TYPE)
            .vehicleNumber(UPDATED_VEHICLE_NUMBER)
            .numberOfSeats(UPDATED_NUMBER_OF_SEATS)
            .vehicleColor(UPDATED_VEHICLE_COLOR)
            .vehicleBrand(UPDATED_VEHICLE_BRAND)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    public void initTest() {
        vehicle = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVehicle != null) {
            vehicleRepository.delete(insertedVehicle);
            insertedVehicle = null;
        }
    }

    @Test
    @Transactional
    void createVehicle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);
        var returnedVehicleDTO = om.readValue(
            restVehicleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VehicleDTO.class
        );

        // Validate the Vehicle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVehicle = vehicleMapper.toEntity(returnedVehicleDTO);
        assertVehicleUpdatableFieldsEquals(returnedVehicle, getPersistedVehicle(returnedVehicle));

        insertedVehicle = returnedVehicle;
    }

    @Test
    @Transactional
    void createVehicleWithExistingId() throws Exception {
        // Create the Vehicle with an existing ID
        vehicle.setId(1L);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicles() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get all the vehicleList
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleID").value(hasItem(DEFAULT_VEHICLE_ID.toString())))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vehicleImageContentType").value(hasItem(DEFAULT_VEHICLE_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].vehicleImage").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_VEHICLE_IMAGE))))
            .andExpect(jsonPath("$.[*].carregistrationContentType").value(hasItem(DEFAULT_CARREGISTRATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].carregistration").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CARREGISTRATION))))
            .andExpect(
                jsonPath("$.[*].vehicleInspectionCertificateContentType").value(
                    hasItem(DEFAULT_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE)
                )
            )
            .andExpect(
                jsonPath("$.[*].vehicleInspectionCertificate").value(
                    hasItem(Base64.getEncoder().encodeToString(DEFAULT_VEHICLE_INSPECTION_CERTIFICATE))
                )
            )
            .andExpect(jsonPath("$.[*].carInsuranceContentType").value(hasItem(DEFAULT_CAR_INSURANCE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].carInsurance").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_CAR_INSURANCE))))
            .andExpect(jsonPath("$.[*].vehicleNumber").value(hasItem(DEFAULT_VEHICLE_NUMBER)))
            .andExpect(jsonPath("$.[*].numberOfSeats").value(hasItem(DEFAULT_NUMBER_OF_SEATS)))
            .andExpect(jsonPath("$.[*].vehicleColor").value(hasItem(DEFAULT_VEHICLE_COLOR)))
            .andExpect(jsonPath("$.[*].vehicleBrand").value(hasItem(DEFAULT_VEHICLE_BRAND)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        // Get the vehicle
        restVehicleMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicle.getId().intValue()))
            .andExpect(jsonPath("$.vehicleID").value(DEFAULT_VEHICLE_ID.toString()))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.vehicleImageContentType").value(DEFAULT_VEHICLE_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.vehicleImage").value(Base64.getEncoder().encodeToString(DEFAULT_VEHICLE_IMAGE)))
            .andExpect(jsonPath("$.carregistrationContentType").value(DEFAULT_CARREGISTRATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.carregistration").value(Base64.getEncoder().encodeToString(DEFAULT_CARREGISTRATION)))
            .andExpect(jsonPath("$.vehicleInspectionCertificateContentType").value(DEFAULT_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE))
            .andExpect(
                jsonPath("$.vehicleInspectionCertificate").value(Base64.getEncoder().encodeToString(DEFAULT_VEHICLE_INSPECTION_CERTIFICATE))
            )
            .andExpect(jsonPath("$.carInsuranceContentType").value(DEFAULT_CAR_INSURANCE_CONTENT_TYPE))
            .andExpect(jsonPath("$.carInsurance").value(Base64.getEncoder().encodeToString(DEFAULT_CAR_INSURANCE)))
            .andExpect(jsonPath("$.vehicleNumber").value(DEFAULT_VEHICLE_NUMBER))
            .andExpect(jsonPath("$.numberOfSeats").value(DEFAULT_NUMBER_OF_SEATS))
            .andExpect(jsonPath("$.vehicleColor").value(DEFAULT_VEHICLE_COLOR))
            .andExpect(jsonPath("$.vehicleBrand").value(DEFAULT_VEHICLE_BRAND))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVehicle() throws Exception {
        // Get the vehicle
        restVehicleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle
        Vehicle updatedVehicle = vehicleRepository.findById(vehicle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicle are not directly saved in db
        em.detach(updatedVehicle);
        updatedVehicle
            .vehicleID(UPDATED_VEHICLE_ID)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleImage(UPDATED_VEHICLE_IMAGE)
            .vehicleImageContentType(UPDATED_VEHICLE_IMAGE_CONTENT_TYPE)
            .carregistration(UPDATED_CARREGISTRATION)
            .carregistrationContentType(UPDATED_CARREGISTRATION_CONTENT_TYPE)
            .vehicleInspectionCertificate(UPDATED_VEHICLE_INSPECTION_CERTIFICATE)
            .vehicleInspectionCertificateContentType(UPDATED_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE)
            .carInsurance(UPDATED_CAR_INSURANCE)
            .carInsuranceContentType(UPDATED_CAR_INSURANCE_CONTENT_TYPE)
            .vehicleNumber(UPDATED_VEHICLE_NUMBER)
            .numberOfSeats(UPDATED_NUMBER_OF_SEATS)
            .vehicleColor(UPDATED_VEHICLE_COLOR)
            .vehicleBrand(UPDATED_VEHICLE_BRAND)
            .status(UPDATED_STATUS);
        VehicleDTO vehicleDTO = vehicleMapper.toDto(updatedVehicle);

        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVehicleToMatchAllProperties(updatedVehicle);
    }

    @Test
    @Transactional
    void putNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .vehicleID(UPDATED_VEHICLE_ID)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleImage(UPDATED_VEHICLE_IMAGE)
            .vehicleImageContentType(UPDATED_VEHICLE_IMAGE_CONTENT_TYPE)
            .carInsurance(UPDATED_CAR_INSURANCE)
            .carInsuranceContentType(UPDATED_CAR_INSURANCE_CONTENT_TYPE);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVehicle, vehicle), getPersistedVehicle(vehicle));
    }

    @Test
    @Transactional
    void fullUpdateVehicleWithPatch() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vehicle using partial update
        Vehicle partialUpdatedVehicle = new Vehicle();
        partialUpdatedVehicle.setId(vehicle.getId());

        partialUpdatedVehicle
            .vehicleID(UPDATED_VEHICLE_ID)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .vehicleImage(UPDATED_VEHICLE_IMAGE)
            .vehicleImageContentType(UPDATED_VEHICLE_IMAGE_CONTENT_TYPE)
            .carregistration(UPDATED_CARREGISTRATION)
            .carregistrationContentType(UPDATED_CARREGISTRATION_CONTENT_TYPE)
            .vehicleInspectionCertificate(UPDATED_VEHICLE_INSPECTION_CERTIFICATE)
            .vehicleInspectionCertificateContentType(UPDATED_VEHICLE_INSPECTION_CERTIFICATE_CONTENT_TYPE)
            .carInsurance(UPDATED_CAR_INSURANCE)
            .carInsuranceContentType(UPDATED_CAR_INSURANCE_CONTENT_TYPE)
            .vehicleNumber(UPDATED_VEHICLE_NUMBER)
            .numberOfSeats(UPDATED_NUMBER_OF_SEATS)
            .vehicleColor(UPDATED_VEHICLE_COLOR)
            .vehicleBrand(UPDATED_VEHICLE_BRAND)
            .status(UPDATED_STATUS);

        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVehicle))
            )
            .andExpect(status().isOk());

        // Validate the Vehicle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVehicleUpdatableFieldsEquals(partialUpdatedVehicle, getPersistedVehicle(partialUpdatedVehicle));
    }

    @Test
    @Transactional
    void patchNonExistingVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vehicleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vehicle.setId(longCount.incrementAndGet());

        // Create the Vehicle
        VehicleDTO vehicleDTO = vehicleMapper.toDto(vehicle);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vehicleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vehicle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicle() throws Exception {
        // Initialize the database
        insertedVehicle = vehicleRepository.saveAndFlush(vehicle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vehicle
        restVehicleMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vehicleRepository.count();
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

    protected Vehicle getPersistedVehicle(Vehicle vehicle) {
        return vehicleRepository.findById(vehicle.getId()).orElseThrow();
    }

    protected void assertPersistedVehicleToMatchAllProperties(Vehicle expectedVehicle) {
        assertVehicleAllPropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }

    protected void assertPersistedVehicleToMatchUpdatableProperties(Vehicle expectedVehicle) {
        assertVehicleAllUpdatablePropertiesEquals(expectedVehicle, getPersistedVehicle(expectedVehicle));
    }
}
