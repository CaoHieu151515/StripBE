// package strip.web.rest;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.hamcrest.Matchers.hasItem;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static strip.domain.RequestTripAsserts.*;
// import static strip.web.rest.TestUtil.createUpdateProxyForBean;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.persistence.EntityManager;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.Base64;
// import java.util.Random;
// import java.util.UUID;
// import java.util.concurrent.atomic.AtomicLong;
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.http.MediaType;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.transaction.annotation.Transactional;
// import strip.IntegrationTest;
// import strip.domain.RequestTrip;
// import strip.domain.enumeration.PassengerStatus;
// import strip.domain.enumeration.PassengerType;
// import strip.repository.RequestTripRepository;
// import strip.repository.UserRepository;
// import strip.service.dto.RequestTripDTO;
// import strip.service.mapper.RequestTripMapper;

// /**
//  * Integration tests for the {@link RequestTripResource} REST controller.
//  */
// @IntegrationTest
// @AutoConfigureMockMvc
// @WithMockUser
// class RequestTripResourceIT {

//     private static final UUID DEFAULT_REQUEST_TRIP_ID = UUID.randomUUID();
//     private static final UUID UPDATED_REQUEST_TRIP_ID = UUID.randomUUID();

//     private static final String DEFAULT_START_LOCA = "AAAAAAAAAA";
//     private static final String UPDATED_START_LOCA = "BBBBBBBBBB";

//     private static final String DEFAULT_END_LOCA = "AAAAAAAAAA";
//     private static final String UPDATED_END_LOCA = "BBBBBBBBBB";

//     private static final Double DEFAULT_AMOUNT_APPROVE_FEE = 1D;
//     private static final Double UPDATED_AMOUNT_APPROVE_FEE = 2D;

//     private static final Integer DEFAULT_NUMBEROF_SEATS = 1;
//     private static final Integer UPDATED_NUMBEROF_SEATS = 2;

//     private static final byte[] DEFAULT_LUGGAGE_IMG = TestUtil.createByteArray(1, "0");
//     private static final byte[] UPDATED_LUGGAGE_IMG = TestUtil.createByteArray(1, "1");
//     private static final String DEFAULT_LUGGAGE_IMG_CONTENT_TYPE = "image/jpg";
//     private static final String UPDATED_LUGGAGE_IMG_CONTENT_TYPE = "image/png";

//     private static final String DEFAULT_LUGGAGE_DESCRIPTION = "AAAAAAAAAA";
//     private static final String UPDATED_LUGGAGE_DESCRIPTION = "BBBBBBBBBB";

//     private static final PassengerType DEFAULT_TYPE = PassengerType.LUGGAGE;
//     private static final PassengerType UPDATED_TYPE = PassengerType.PASSENGER;

//     private static final PassengerStatus DEFAULT_STATUS = PassengerStatus.WAITING;
//     private static final PassengerStatus UPDATED_STATUS = PassengerStatus.BOOKED;

//     private static final Instant DEFAULT_PICK_UP_TIME = Instant.ofEpochMilli(0L);
//     private static final Instant UPDATED_PICK_UP_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

//     private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
//     private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

//     private static final Boolean DEFAULT_CHECK_IN = false;
//     private static final Boolean UPDATED_CHECK_IN = true;

//     private static final Instant DEFAULT_CHECK_IN_TIME = Instant.ofEpochMilli(0L);
//     private static final Instant UPDATED_CHECK_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

//     private static final Boolean DEFAULT_CHECK_OUT = false;
//     private static final Boolean UPDATED_CHECK_OUT = true;

//     private static final Instant DEFAULT_CHECK_OUT_T_IME = Instant.ofEpochMilli(0L);
//     private static final Instant UPDATED_CHECK_OUT_T_IME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

//     private static final Instant DEFAULT_APPLIED_AT = Instant.ofEpochMilli(0L);
//     private static final Instant UPDATED_APPLIED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

//     private static final String ENTITY_API_URL = "/api/request-trips";
//     private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

//     private static Random random = new Random();
//     private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

//     @Autowired
//     private ObjectMapper om;

//     @Autowired
//     private RequestTripRepository requestTripRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private RequestTripMapper requestTripMapper;

//     @Autowired
//     private EntityManager em;

//     @Autowired
//     private MockMvc restRequestTripMockMvc;

//     private RequestTrip requestTrip;

//     private RequestTrip insertedRequestTrip;

//     /**
//      * Create an entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static RequestTrip createEntity() {
//         return new RequestTrip()
//             .requestTripID(DEFAULT_REQUEST_TRIP_ID)
//             .startLoca(DEFAULT_START_LOCA)
//             .endLoca(DEFAULT_END_LOCA)
//             .amountApproveFee(DEFAULT_AMOUNT_APPROVE_FEE)
//             .numberofSeats(DEFAULT_NUMBEROF_SEATS)
//             .luggageImg(DEFAULT_LUGGAGE_IMG)
//             .luggageImgContentType(DEFAULT_LUGGAGE_IMG_CONTENT_TYPE)
//             .luggageDescription(DEFAULT_LUGGAGE_DESCRIPTION)
//             .type(DEFAULT_TYPE)
//             .status(DEFAULT_STATUS)
//             .pickUpTime(DEFAULT_PICK_UP_TIME)
//             .endTime(DEFAULT_END_TIME)
//             .checkIn(DEFAULT_CHECK_IN)
//             .checkInTime(DEFAULT_CHECK_IN_TIME)
//             .checkOut(DEFAULT_CHECK_OUT)
//             .checkOutTIme(DEFAULT_CHECK_OUT_T_IME)
//             .appliedAt(DEFAULT_APPLIED_AT);
//     }

//     /**
//      * Create an updated entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static RequestTrip createUpdatedEntity() {
//         return new RequestTrip()
//             .requestTripID(UPDATED_REQUEST_TRIP_ID)
//             .startLoca(UPDATED_START_LOCA)
//             .endLoca(UPDATED_END_LOCA)
//             .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
//             .numberofSeats(UPDATED_NUMBEROF_SEATS)
//             .luggageImg(UPDATED_LUGGAGE_IMG)
//             .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
//             .luggageDescription(UPDATED_LUGGAGE_DESCRIPTION)
//             .type(UPDATED_TYPE)
//             .status(UPDATED_STATUS)
//             .pickUpTime(UPDATED_PICK_UP_TIME)
//             .endTime(UPDATED_END_TIME)
//             .checkIn(UPDATED_CHECK_IN)
//             .checkInTime(UPDATED_CHECK_IN_TIME)
//             .checkOut(UPDATED_CHECK_OUT)
//             .checkOutTIme(UPDATED_CHECK_OUT_T_IME)
//             .appliedAt(UPDATED_APPLIED_AT);
//     }

//     @BeforeEach
//     public void initTest() {
//         requestTrip = createEntity();
//     }

//     @AfterEach
//     public void cleanup() {
//         if (insertedRequestTrip != null) {
//             requestTripRepository.delete(insertedRequestTrip);
//             insertedRequestTrip = null;
//         }
//     }

//     @Test
//     @Transactional
//     void createRequestTrip() throws Exception {
//         long databaseSizeBeforeCreate = getRepositoryCount();
//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);
//         var returnedRequestTripDTO = om.readValue(
//             restRequestTripMockMvc
//                 .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestTripDTO)))
//                 .andExpect(status().isCreated())
//                 .andReturn()
//                 .getResponse()
//                 .getContentAsString(),
//             RequestTripDTO.class
//         );

//         // Validate the RequestTrip in the database
//         assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
//         var returnedRequestTrip = requestTripMapper.toEntity(returnedRequestTripDTO);
//         assertRequestTripUpdatableFieldsEquals(returnedRequestTrip, getPersistedRequestTrip(returnedRequestTrip));

//         insertedRequestTrip = returnedRequestTrip;
//     }

//     @Test
//     @Transactional
//     void createRequestTripWithExistingId() throws Exception {
//         // Create the RequestTrip with an existing ID
//         requestTrip.setId(1L);
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         long databaseSizeBeforeCreate = getRepositoryCount();

//         // An entity with an existing ID cannot be created, so this API call must fail
//         restRequestTripMockMvc
//             .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestTripDTO)))
//             .andExpect(status().isBadRequest());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeCreate);
//     }

//     @Test
//     @Transactional
//     void getAllRequestTrips() throws Exception {
//         // Initialize the database
//         insertedRequestTrip = requestTripRepository.saveAndFlush(requestTrip);

//         // Get all the requestTripList
//         restRequestTripMockMvc
//             .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.[*].id").value(hasItem(requestTrip.getId().intValue())))
//             .andExpect(jsonPath("$.[*].requestTripID").value(hasItem(DEFAULT_REQUEST_TRIP_ID.toString())))
//             .andExpect(jsonPath("$.[*].startLoca").value(hasItem(DEFAULT_START_LOCA)))
//             .andExpect(jsonPath("$.[*].endLoca").value(hasItem(DEFAULT_END_LOCA)))
//             .andExpect(jsonPath("$.[*].amountApproveFee").value(hasItem(DEFAULT_AMOUNT_APPROVE_FEE)))
//             .andExpect(jsonPath("$.[*].numberofSeats").value(hasItem(DEFAULT_NUMBEROF_SEATS)))
//             .andExpect(jsonPath("$.[*].luggageImgContentType").value(hasItem(DEFAULT_LUGGAGE_IMG_CONTENT_TYPE)))
//             .andExpect(jsonPath("$.[*].luggageImg").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LUGGAGE_IMG))))
//             .andExpect(jsonPath("$.[*].luggageDescription").value(hasItem(DEFAULT_LUGGAGE_DESCRIPTION)))
//             .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
//             .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
//             .andExpect(jsonPath("$.[*].pickUpTime").value(hasItem(DEFAULT_PICK_UP_TIME.toString())))
//             .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
//             .andExpect(jsonPath("$.[*].checkIn").value(hasItem(DEFAULT_CHECK_IN)))
//             .andExpect(jsonPath("$.[*].checkInTime").value(hasItem(DEFAULT_CHECK_IN_TIME.toString())))
//             .andExpect(jsonPath("$.[*].checkOut").value(hasItem(DEFAULT_CHECK_OUT)))
//             .andExpect(jsonPath("$.[*].checkOutTIme").value(hasItem(DEFAULT_CHECK_OUT_T_IME.toString())))
//             .andExpect(jsonPath("$.[*].appliedAt").value(hasItem(DEFAULT_APPLIED_AT.toString())));
//     }

//     @Test
//     @Transactional
//     void getRequestTrip() throws Exception {
//         // Initialize the database
//         insertedRequestTrip = requestTripRepository.saveAndFlush(requestTrip);

//         // Get the requestTrip
//         restRequestTripMockMvc
//             .perform(get(ENTITY_API_URL_ID, requestTrip.getId()))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.id").value(requestTrip.getId().intValue()))
//             .andExpect(jsonPath("$.requestTripID").value(DEFAULT_REQUEST_TRIP_ID.toString()))
//             .andExpect(jsonPath("$.startLoca").value(DEFAULT_START_LOCA))
//             .andExpect(jsonPath("$.endLoca").value(DEFAULT_END_LOCA))
//             .andExpect(jsonPath("$.amountApproveFee").value(DEFAULT_AMOUNT_APPROVE_FEE))
//             .andExpect(jsonPath("$.numberofSeats").value(DEFAULT_NUMBEROF_SEATS))
//             .andExpect(jsonPath("$.luggageImgContentType").value(DEFAULT_LUGGAGE_IMG_CONTENT_TYPE))
//             .andExpect(jsonPath("$.luggageImg").value(Base64.getEncoder().encodeToString(DEFAULT_LUGGAGE_IMG)))
//             .andExpect(jsonPath("$.luggageDescription").value(DEFAULT_LUGGAGE_DESCRIPTION))
//             .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
//             .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
//             .andExpect(jsonPath("$.pickUpTime").value(DEFAULT_PICK_UP_TIME.toString()))
//             .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
//             .andExpect(jsonPath("$.checkIn").value(DEFAULT_CHECK_IN))
//             .andExpect(jsonPath("$.checkInTime").value(DEFAULT_CHECK_IN_TIME.toString()))
//             .andExpect(jsonPath("$.checkOut").value(DEFAULT_CHECK_OUT))
//             .andExpect(jsonPath("$.checkOutTIme").value(DEFAULT_CHECK_OUT_T_IME.toString()))
//             .andExpect(jsonPath("$.appliedAt").value(DEFAULT_APPLIED_AT.toString()));
//     }

//     @Test
//     @Transactional
//     void getNonExistingRequestTrip() throws Exception {
//         // Get the requestTrip
//         restRequestTripMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     void putExistingRequestTrip() throws Exception {
//         // Initialize the database
//         insertedRequestTrip = requestTripRepository.saveAndFlush(requestTrip);

//         long databaseSizeBeforeUpdate = getRepositoryCount();

//         // Update the requestTrip
//         RequestTrip updatedRequestTrip = requestTripRepository.findById(requestTrip.getId()).orElseThrow();
//         // Disconnect from session so that the updates on updatedRequestTrip are not directly saved in db
//         em.detach(updatedRequestTrip);
//         updatedRequestTrip
//             .requestTripID(UPDATED_REQUEST_TRIP_ID)
//             .startLoca(UPDATED_START_LOCA)
//             .endLoca(UPDATED_END_LOCA)
//             .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
//             .numberofSeats(UPDATED_NUMBEROF_SEATS)
//             .luggageImg(UPDATED_LUGGAGE_IMG)
//             .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
//             .luggageDescription(UPDATED_LUGGAGE_DESCRIPTION)
//             .type(UPDATED_TYPE)
//             .status(UPDATED_STATUS)
//             .pickUpTime(UPDATED_PICK_UP_TIME)
//             .endTime(UPDATED_END_TIME)
//             .checkIn(UPDATED_CHECK_IN)
//             .checkInTime(UPDATED_CHECK_IN_TIME)
//             .checkOut(UPDATED_CHECK_OUT)
//             .checkOutTIme(UPDATED_CHECK_OUT_T_IME)
//             .appliedAt(UPDATED_APPLIED_AT);
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(updatedRequestTrip);

//         restRequestTripMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, requestTripDTO.getId())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(om.writeValueAsBytes(requestTripDTO))
//             )
//             .andExpect(status().isOk());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//         assertPersistedRequestTripToMatchAllProperties(updatedRequestTrip);
//     }

//     @Test
//     @Transactional
//     void putNonExistingRequestTrip() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         requestTrip.setId(longCount.incrementAndGet());

//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restRequestTripMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, requestTripDTO.getId())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(om.writeValueAsBytes(requestTripDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void putWithIdMismatchRequestTrip() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         requestTrip.setId(longCount.incrementAndGet());

//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restRequestTripMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(om.writeValueAsBytes(requestTripDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void putWithMissingIdPathParamRequestTrip() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         requestTrip.setId(longCount.incrementAndGet());

//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restRequestTripMockMvc
//             .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(requestTripDTO)))
//             .andExpect(status().isMethodNotAllowed());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void partialUpdateRequestTripWithPatch() throws Exception {
//         // Initialize the database
//         insertedRequestTrip = requestTripRepository.saveAndFlush(requestTrip);

//         long databaseSizeBeforeUpdate = getRepositoryCount();

//         // Update the requestTrip using partial update
//         RequestTrip partialUpdatedRequestTrip = new RequestTrip();
//         partialUpdatedRequestTrip.setId(requestTrip.getId());

//         partialUpdatedRequestTrip
//             .startLoca(UPDATED_START_LOCA)
//             .endLoca(UPDATED_END_LOCA)
//             .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
//             .luggageImg(UPDATED_LUGGAGE_IMG)
//             .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
//             .type(UPDATED_TYPE)
//             .status(UPDATED_STATUS)
//             .pickUpTime(UPDATED_PICK_UP_TIME)
//             .checkIn(UPDATED_CHECK_IN)
//             .checkOut(UPDATED_CHECK_OUT)
//             .checkOutTIme(UPDATED_CHECK_OUT_T_IME);

//         restRequestTripMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, partialUpdatedRequestTrip.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(partialUpdatedRequestTrip))
//             )
//             .andExpect(status().isOk());

//         // Validate the RequestTrip in the database

//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//         assertRequestTripUpdatableFieldsEquals(
//             createUpdateProxyForBean(partialUpdatedRequestTrip, requestTrip),
//             getPersistedRequestTrip(requestTrip)
//         );
//     }

//     @Test
//     @Transactional
//     void fullUpdateRequestTripWithPatch() throws Exception {
//         // Initialize the database
//         insertedRequestTrip = requestTripRepository.saveAndFlush(requestTrip);

//         long databaseSizeBeforeUpdate = getRepositoryCount();

//         // Update the requestTrip using partial update
//         RequestTrip partialUpdatedRequestTrip = new RequestTrip();
//         partialUpdatedRequestTrip.setId(requestTrip.getId());

//         partialUpdatedRequestTrip
//             .requestTripID(UPDATED_REQUEST_TRIP_ID)
//             .startLoca(UPDATED_START_LOCA)
//             .endLoca(UPDATED_END_LOCA)
//             .amountApproveFee(UPDATED_AMOUNT_APPROVE_FEE)
//             .numberofSeats(UPDATED_NUMBEROF_SEATS)
//             .luggageImg(UPDATED_LUGGAGE_IMG)
//             .luggageImgContentType(UPDATED_LUGGAGE_IMG_CONTENT_TYPE)
//             .luggageDescription(UPDATED_LUGGAGE_DESCRIPTION)
//             .type(UPDATED_TYPE)
//             .status(UPDATED_STATUS)
//             .pickUpTime(UPDATED_PICK_UP_TIME)
//             .endTime(UPDATED_END_TIME)
//             .checkIn(UPDATED_CHECK_IN)
//             .checkInTime(UPDATED_CHECK_IN_TIME)
//             .checkOut(UPDATED_CHECK_OUT)
//             .checkOutTIme(UPDATED_CHECK_OUT_T_IME)
//             .appliedAt(UPDATED_APPLIED_AT);

//         restRequestTripMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, partialUpdatedRequestTrip.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(partialUpdatedRequestTrip))
//             )
//             .andExpect(status().isOk());

//         // Validate the RequestTrip in the database

//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//         assertRequestTripUpdatableFieldsEquals(partialUpdatedRequestTrip, getPersistedRequestTrip(partialUpdatedRequestTrip));
//     }

//     @Test
//     @Transactional
//     void patchNonExistingRequestTrip() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         requestTrip.setId(longCount.incrementAndGet());

//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restRequestTripMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, requestTripDTO.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(requestTripDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void patchWithIdMismatchRequestTrip() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         requestTrip.setId(longCount.incrementAndGet());

//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restRequestTripMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(requestTripDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void patchWithMissingIdPathParamRequestTrip() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         requestTrip.setId(longCount.incrementAndGet());

//         // Create the RequestTrip
//         RequestTripDTO requestTripDTO = requestTripMapper.toDto(requestTrip);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restRequestTripMockMvc
//             .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(requestTripDTO)))
//             .andExpect(status().isMethodNotAllowed());

//         // Validate the RequestTrip in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void deleteRequestTrip() throws Exception {
//         // Initialize the database
//         insertedRequestTrip = requestTripRepository.saveAndFlush(requestTrip);

//         long databaseSizeBeforeDelete = getRepositoryCount();

//         // Delete the requestTrip
//         restRequestTripMockMvc
//             .perform(delete(ENTITY_API_URL_ID, requestTrip.getId()).accept(MediaType.APPLICATION_JSON))
//             .andExpect(status().isNoContent());

//         // Validate the database contains one less item
//         assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
//     }

//     protected long getRepositoryCount() {
//         return requestTripRepository.count();
//     }

//     protected void assertIncrementedRepositoryCount(long countBefore) {
//         assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
//     }

//     protected void assertDecrementedRepositoryCount(long countBefore) {
//         assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
//     }

//     protected void assertSameRepositoryCount(long countBefore) {
//         assertThat(countBefore).isEqualTo(getRepositoryCount());
//     }

//     protected RequestTrip getPersistedRequestTrip(RequestTrip requestTrip) {
//         return requestTripRepository.findById(requestTrip.getId()).orElseThrow();
//     }

//     protected void assertPersistedRequestTripToMatchAllProperties(RequestTrip expectedRequestTrip) {
//         assertRequestTripAllPropertiesEquals(expectedRequestTrip, getPersistedRequestTrip(expectedRequestTrip));
//     }

//     protected void assertPersistedRequestTripToMatchUpdatableProperties(RequestTrip expectedRequestTrip) {
//         assertRequestTripAllUpdatablePropertiesEquals(expectedRequestTrip, getPersistedRequestTrip(expectedRequestTrip));
//     }
// }
