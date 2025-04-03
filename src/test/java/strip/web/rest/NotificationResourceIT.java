// package strip.web.rest;

// import static org.assertj.core.api.Assertions.assertThat;
// import static org.hamcrest.Matchers.hasItem;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static strip.domain.NotificationAsserts.*;
// import static strip.web.rest.TestUtil.createUpdateProxyForBean;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import jakarta.persistence.EntityManager;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.Random;
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
// import strip.domain.Notification;
// import strip.repository.NotificationRepository;
// import strip.repository.UserRepository;
// import strip.service.dto.NotificationDTO;
// import strip.service.mapper.NotificationMapper;

// /**
//  * Integration tests for the {@link NotificationResource} REST controller.
//  */
// @IntegrationTest
// @AutoConfigureMockMvc
// @WithMockUser
// class NotificationResourceIT {

//     private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
//     private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

//     private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
//     private static final String UPDATED_CONTENT = "BBBBBBBBBB";

//     private static final String ENTITY_API_URL = "/api/notifications";
//     private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

//     private static Random random = new Random();
//     private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

//     @Autowired
//     private ObjectMapper om;

//     @Autowired
//     private NotificationRepository notificationRepository;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private NotificationMapper notificationMapper;

//     @Autowired
//     private EntityManager em;

//     @Autowired
//     private MockMvc restNotificationMockMvc;

//     private Notification notification;

//     private Notification insertedNotification;

//     /**
//      * Create an entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static Notification createEntity() {
//         return new Notification().date(DEFAULT_DATE).content(DEFAULT_CONTENT);
//     }

//     /**
//      * Create an updated entity for this test.
//      *
//      * This is a static method, as tests for other entities might also need it,
//      * if they test an entity which requires the current entity.
//      */
//     public static Notification createUpdatedEntity() {
//         return new Notification().date(UPDATED_DATE).content(UPDATED_CONTENT);
//     }

//     @BeforeEach
//     public void initTest() {
//         notification = createEntity();
//     }

//     @AfterEach
//     public void cleanup() {
//         if (insertedNotification != null) {
//             notificationRepository.delete(insertedNotification);
//             insertedNotification = null;
//         }
//     }

//     @Test
//     @Transactional
//     void createNotification() throws Exception {
//         long databaseSizeBeforeCreate = getRepositoryCount();
//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);
//         var returnedNotificationDTO = om.readValue(
//             restNotificationMockMvc
//                 .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
//                 .andExpect(status().isCreated())
//                 .andReturn()
//                 .getResponse()
//                 .getContentAsString(),
//             NotificationDTO.class
//         );

//         // Validate the Notification in the database
//         assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
//         var returnedNotification = notificationMapper.toEntity(returnedNotificationDTO);
//         assertNotificationUpdatableFieldsEquals(returnedNotification, getPersistedNotification(returnedNotification));

//         insertedNotification = returnedNotification;
//     }

//     @Test
//     @Transactional
//     void createNotificationWithExistingId() throws Exception {
//         // Create the Notification with an existing ID
//         notification.setId(1L);
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         long databaseSizeBeforeCreate = getRepositoryCount();

//         // An entity with an existing ID cannot be created, so this API call must fail
//         restNotificationMockMvc
//             .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
//             .andExpect(status().isBadRequest());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeCreate);
//     }

//     @Test
//     @Transactional
//     void getAllNotifications() throws Exception {
//         // Initialize the database
//         insertedNotification = notificationRepository.saveAndFlush(notification);

//         // Get all the notificationList
//         restNotificationMockMvc
//             .perform(get(ENTITY_API_URL + "?sort=id,desc"))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
//             .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
//             .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
//     }

//     @Test
//     @Transactional
//     void getNotification() throws Exception {
//         // Initialize the database
//         insertedNotification = notificationRepository.saveAndFlush(notification);

//         // Get the notification
//         restNotificationMockMvc
//             .perform(get(ENTITY_API_URL_ID, notification.getId()))
//             .andExpect(status().isOk())
//             .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//             .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
//             .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
//             .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
//     }

//     @Test
//     @Transactional
//     void getNonExistingNotification() throws Exception {
//         // Get the notification
//         restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
//     }

//     @Test
//     @Transactional
//     void putExistingNotification() throws Exception {
//         // Initialize the database
//         insertedNotification = notificationRepository.saveAndFlush(notification);

//         long databaseSizeBeforeUpdate = getRepositoryCount();

//         // Update the notification
//         Notification updatedNotification = notificationRepository.findById(notification.getId()).orElseThrow();
//         // Disconnect from session so that the updates on updatedNotification are not directly saved in db
//         em.detach(updatedNotification);
//         updatedNotification.date(UPDATED_DATE).content(UPDATED_CONTENT);
//         NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

//         restNotificationMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, notificationDTO.getId())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(om.writeValueAsBytes(notificationDTO))
//             )
//             .andExpect(status().isOk());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//         assertPersistedNotificationToMatchAllProperties(updatedNotification);
//     }

//     @Test
//     @Transactional
//     void putNonExistingNotification() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         notification.setId(longCount.incrementAndGet());

//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restNotificationMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, notificationDTO.getId())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(om.writeValueAsBytes(notificationDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void putWithIdMismatchNotification() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         notification.setId(longCount.incrementAndGet());

//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restNotificationMockMvc
//             .perform(
//                 put(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                     .contentType(MediaType.APPLICATION_JSON)
//                     .content(om.writeValueAsBytes(notificationDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void putWithMissingIdPathParamNotification() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         notification.setId(longCount.incrementAndGet());

//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restNotificationMockMvc
//             .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
//             .andExpect(status().isMethodNotAllowed());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void partialUpdateNotificationWithPatch() throws Exception {
//         // Initialize the database
//         insertedNotification = notificationRepository.saveAndFlush(notification);

//         long databaseSizeBeforeUpdate = getRepositoryCount();

//         // Update the notification using partial update
//         Notification partialUpdatedNotification = new Notification();
//         partialUpdatedNotification.setId(notification.getId());

//         restNotificationMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(partialUpdatedNotification))
//             )
//             .andExpect(status().isOk());

//         // Validate the Notification in the database

//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//         assertNotificationUpdatableFieldsEquals(
//             createUpdateProxyForBean(partialUpdatedNotification, notification),
//             getPersistedNotification(notification)
//         );
//     }

//     @Test
//     @Transactional
//     void fullUpdateNotificationWithPatch() throws Exception {
//         // Initialize the database
//         insertedNotification = notificationRepository.saveAndFlush(notification);

//         long databaseSizeBeforeUpdate = getRepositoryCount();

//         // Update the notification using partial update
//         Notification partialUpdatedNotification = new Notification();
//         partialUpdatedNotification.setId(notification.getId());

//         partialUpdatedNotification.date(UPDATED_DATE).content(UPDATED_CONTENT);

//         restNotificationMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(partialUpdatedNotification))
//             )
//             .andExpect(status().isOk());

//         // Validate the Notification in the database

//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//         assertNotificationUpdatableFieldsEquals(partialUpdatedNotification, getPersistedNotification(partialUpdatedNotification));
//     }

//     @Test
//     @Transactional
//     void patchNonExistingNotification() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         notification.setId(longCount.incrementAndGet());

//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         // If the entity doesn't have an ID, it will throw BadRequestAlertException
//         restNotificationMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, notificationDTO.getId())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(notificationDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void patchWithIdMismatchNotification() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         notification.setId(longCount.incrementAndGet());

//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restNotificationMockMvc
//             .perform(
//                 patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
//                     .contentType("application/merge-patch+json")
//                     .content(om.writeValueAsBytes(notificationDTO))
//             )
//             .andExpect(status().isBadRequest());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void patchWithMissingIdPathParamNotification() throws Exception {
//         long databaseSizeBeforeUpdate = getRepositoryCount();
//         notification.setId(longCount.incrementAndGet());

//         // Create the Notification
//         NotificationDTO notificationDTO = notificationMapper.toDto(notification);

//         // If url ID doesn't match entity ID, it will throw BadRequestAlertException
//         restNotificationMockMvc
//             .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationDTO)))
//             .andExpect(status().isMethodNotAllowed());

//         // Validate the Notification in the database
//         assertSameRepositoryCount(databaseSizeBeforeUpdate);
//     }

//     @Test
//     @Transactional
//     void deleteNotification() throws Exception {
//         // Initialize the database
//         insertedNotification = notificationRepository.saveAndFlush(notification);

//         long databaseSizeBeforeDelete = getRepositoryCount();

//         // Delete the notification
//         restNotificationMockMvc
//             .perform(delete(ENTITY_API_URL_ID, notification.getId()).accept(MediaType.APPLICATION_JSON))
//             .andExpect(status().isNoContent());

//         // Validate the database contains one less item
//         assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
//     }

//     protected long getRepositoryCount() {
//         return notificationRepository.count();
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

//     protected Notification getPersistedNotification(Notification notification) {
//         return notificationRepository.findById(notification.getId()).orElseThrow();
//     }

//     protected void assertPersistedNotificationToMatchAllProperties(Notification expectedNotification) {
//         assertNotificationAllPropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
//     }

//     protected void assertPersistedNotificationToMatchUpdatableProperties(Notification expectedNotification) {
//         assertNotificationAllUpdatablePropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
//     }
// }
