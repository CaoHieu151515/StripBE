package strip.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static strip.domain.PassengerAsserts.*;
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
import strip.domain.Passenger;
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.PassengerType;
import strip.repository.PassengerRepository;
import strip.repository.UserRepository;
import strip.service.dto.PassengerDTO;
import strip.service.mapper.PassengerMapper;

/**
 * Integration tests for the {@link PassengerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PassengerResourceIT {

    private static final UUID DEFAULT_PASSENGER_ID = UUID.randomUUID();
    private static final UUID UPDATED_PASSENGER_ID = UUID.randomUUID();

    private static final String DEFAULT_START_LOCA = "AAAAAAAAAA";
    private static final String UPDATED_START_LOCA = "BBBBBBBBBB";

    private static final String DEFAULT_END_LOCA = "AAAAAAAAAA";
    private static final String UPDATED_END_LOCA = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT_APPROVE_FEE = 1D;
    private static final Double UPDATED_AMOUNT_APPROVE_FEE = 2D;

    private static final byte[] DEFAULT_LUGGAGE_IMG = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LUGGAGE_IMG = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LUGGAGE_IMG_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LUGGAGE_IMG_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_LUGGAGE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LUGGAGE_DESCRIPTION = "BBBBBBBBBB";

    private static final PassengerType DEFAULT_TYPE = PassengerType.LUGGAGE;
    private static final PassengerType UPDATED_TYPE = PassengerType.PASSENGER;

    private static final PassengerStatus DEFAULT_STATUS = PassengerStatus.WATING;
    private static final PassengerStatus UPDATED_STATUS = PassengerStatus.BOOKED;

    private static final Instant DEFAULT_PICK_UP_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PICK_UP_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CHECK_IN = false;
    private static final Boolean UPDATED_CHECK_IN = true;

    private static final Instant DEFAULT_CHECK_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CHECK_OUT = false;
    private static final Boolean UPDATED_CHECK_OUT = true;

    private static final Instant DEFAULT_CHECK_OUT_T_IME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_OUT_T_IME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_APPLIED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_APPLIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/passengers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    private Passenger insertedPassenger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .passengerID(DEFAULT_PASSENGER_ID)
            .startLoca(DEFAULT_START_LOCA)
            .endLoca(DEFAULT_END_LOCA)
            .amountApproveFee(DEFAULT_AMOUNT_APPROVE_FEE)
            .luggageImg(DEFAULT_LUGGAGE_IMG)
            .luggageImgContentType(DEFAULT_LUGGAGE_IMG_CONTENT_TYPE)
            .luggageDescription(DEFAULT_LUGGAGE_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .pickUpTime(DEFAULT_PICK_UP_TIME)
            .endTime(DEFAULT_END_TIME)
            .checkIn(DEFAULT_CHECK_IN)
            .checkInTime(DEFAULT_CHECK_IN_TIME)
            .checkOut(DEFAULT_CHECK_OUT)
            .checkOutTIme(DEFAULT_CHECK_OUT_T_IME)
            .appliedAt(DEFAULT_APPLIED_AT);
        return passenger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createUpdatedEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .passengerID(UPDATED_PASSENGER_ID)
            .startLoca(UPDATED_START_LOCA)
            .endLoca(UPDATED_END_LOCA)
            .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
            .luggageImg(UPDATED_LUGGAGE_IMG)
            .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
            .luggageDescription(UPDATED_LUGGAGE_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .pickUpTime(UPDATED_PICK_UP_TIME)
            .endTime(UPDATED_END_TIME)
            .checkIn(UPDATED_CHECK_IN)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOut(UPDATED_CHECK_OUT)
            .checkOutTIme(UPDATED_CHECK_OUT_T_IME)
            .appliedAt(UPDATED_APPLIED_AT);
        return passenger;
    }

    @BeforeEach
    public void initTest() {
        passenger = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPassenger != null) {
            passengerRepository.delete(insertedPassenger);
            insertedPassenger = null;
        }
    }

    @Test
    @Transactional
    void createPassenger() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);
        var returnedPassengerDTO = om.readValue(
            restPassengerMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PassengerDTO.class
        );

        // Validate the Passenger in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPassenger = passengerMapper.toEntity(returnedPassengerDTO);
        assertPassengerUpdatableFieldsEquals(returnedPassenger, getPersistedPassenger(returnedPassenger));

        insertedPassenger = returnedPassenger;
    }

    @Test
    @Transactional
    void createPassengerWithExistingId() throws Exception {
        // Create the Passenger with an existing ID
        passenger.setId(1L);
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPassengers() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].passengerID").value(hasItem(DEFAULT_PASSENGER_ID.toString())))
            .andExpect(jsonPath("$.[*].startLoca").value(hasItem(DEFAULT_START_LOCA)))
            .andExpect(jsonPath("$.[*].endLoca").value(hasItem(DEFAULT_END_LOCA)))
            .andExpect(jsonPath("$.[*].amountApproveFee").value(hasItem(DEFAULT_AMOUNT_APPROVE_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].luggageImgContentType").value(hasItem(DEFAULT_LUGGAGE_IMG_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].luggageImg").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LUGGAGE_IMG))))
            .andExpect(jsonPath("$.[*].luggageDescription").value(hasItem(DEFAULT_LUGGAGE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].pickUpTime").value(hasItem(DEFAULT_PICK_UP_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkIn").value(hasItem(DEFAULT_CHECK_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(DEFAULT_CHECK_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkOut").value(hasItem(DEFAULT_CHECK_OUT.booleanValue())))
            .andExpect(jsonPath("$.[*].checkOutTIme").value(hasItem(DEFAULT_CHECK_OUT_T_IME.toString())))
            .andExpect(jsonPath("$.[*].appliedAt").value(hasItem(DEFAULT_APPLIED_AT.toString())));
    }

    @Test
    @Transactional
    void getPassenger() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc
            .perform(get(ENTITY_API_URL_ID, passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.passengerID").value(DEFAULT_PASSENGER_ID.toString()))
            .andExpect(jsonPath("$.startLoca").value(DEFAULT_START_LOCA))
            .andExpect(jsonPath("$.endLoca").value(DEFAULT_END_LOCA))
            .andExpect(jsonPath("$.amountApproveFee").value(DEFAULT_AMOUNT_APPROVE_FEE.doubleValue()))
            .andExpect(jsonPath("$.luggageImgContentType").value(DEFAULT_LUGGAGE_IMG_CONTENT_TYPE))
            .andExpect(jsonPath("$.luggageImg").value(Base64.getEncoder().encodeToString(DEFAULT_LUGGAGE_IMG)))
            .andExpect(jsonPath("$.luggageDescription").value(DEFAULT_LUGGAGE_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.pickUpTime").value(DEFAULT_PICK_UP_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.checkIn").value(DEFAULT_CHECK_IN.booleanValue()))
            .andExpect(jsonPath("$.checkInTime").value(DEFAULT_CHECK_IN_TIME.toString()))
            .andExpect(jsonPath("$.checkOut").value(DEFAULT_CHECK_OUT.booleanValue()))
            .andExpect(jsonPath("$.checkOutTIme").value(DEFAULT_CHECK_OUT_T_IME.toString()))
            .andExpect(jsonPath("$.appliedAt").value(DEFAULT_APPLIED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPassenger() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .passengerID(UPDATED_PASSENGER_ID)
            .startLoca(UPDATED_START_LOCA)
            .endLoca(UPDATED_END_LOCA)
            .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
            .luggageImg(UPDATED_LUGGAGE_IMG)
            .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
            .luggageDescription(UPDATED_LUGGAGE_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .pickUpTime(UPDATED_PICK_UP_TIME)
            .endTime(UPDATED_END_TIME)
            .checkIn(UPDATED_CHECK_IN)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOut(UPDATED_CHECK_OUT)
            .checkOutTIme(UPDATED_CHECK_OUT_T_IME)
            .appliedAt(UPDATED_APPLIED_AT);
        PassengerDTO passengerDTO = passengerMapper.toDto(updatedPassenger);

        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passengerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPassengerToMatchAllProperties(updatedPassenger);
    }

    @Test
    @Transactional
    void putNonExistingPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passengerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassengerWithPatch() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passenger using partial update
        Passenger partialUpdatedPassenger = new Passenger();
        partialUpdatedPassenger.setId(passenger.getId());

        partialUpdatedPassenger
            .endLoca(UPDATED_END_LOCA)
            .type(UPDATED_TYPE)
            .checkIn(UPDATED_CHECK_IN)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOut(UPDATED_CHECK_OUT);

        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassengerUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPassenger, passenger),
            getPersistedPassenger(passenger)
        );
    }

    @Test
    @Transactional
    void fullUpdatePassengerWithPatch() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the passenger using partial update
        Passenger partialUpdatedPassenger = new Passenger();
        partialUpdatedPassenger.setId(passenger.getId());

        partialUpdatedPassenger
            .passengerID(UPDATED_PASSENGER_ID)
            .startLoca(UPDATED_START_LOCA)
            .endLoca(UPDATED_END_LOCA)
            .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
            .luggageImg(UPDATED_LUGGAGE_IMG)
            .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
            .luggageDescription(UPDATED_LUGGAGE_DESCRIPTION)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .pickUpTime(UPDATED_PICK_UP_TIME)
            .endTime(UPDATED_END_TIME)
            .checkIn(UPDATED_CHECK_IN)
            .checkInTime(UPDATED_CHECK_IN_TIME)
            .checkOut(UPDATED_CHECK_OUT)
            .checkOutTIme(UPDATED_CHECK_OUT_T_IME)
            .appliedAt(UPDATED_APPLIED_AT);

        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPassengerUpdatableFieldsEquals(partialUpdatedPassenger, getPersistedPassenger(partialUpdatedPassenger));
    }

    @Test
    @Transactional
    void patchNonExistingPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passengerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(passengerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassenger() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        passenger.setId(longCount.incrementAndGet());

        // Create the Passenger
        PassengerDTO passengerDTO = passengerMapper.toDto(passenger);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(passengerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passenger in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassenger() throws Exception {
        // Initialize the database
        insertedPassenger = passengerRepository.saveAndFlush(passenger);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the passenger
        restPassengerMockMvc
            .perform(delete(ENTITY_API_URL_ID, passenger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return passengerRepository.count();
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

    protected Passenger getPersistedPassenger(Passenger passenger) {
        return passengerRepository.findById(passenger.getId()).orElseThrow();
    }

    protected void assertPersistedPassengerToMatchAllProperties(Passenger expectedPassenger) {
        assertPassengerAllPropertiesEquals(expectedPassenger, getPersistedPassenger(expectedPassenger));
    }

    protected void assertPersistedPassengerToMatchUpdatableProperties(Passenger expectedPassenger) {
        assertPassengerAllUpdatablePropertiesEquals(expectedPassenger, getPersistedPassenger(expectedPassenger));
    }
}
