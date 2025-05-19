package strip.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.Feedback;
import strip.domain.enumeration.FeedbackStatus;
import strip.domain.enumeration.FeedbackType;

/**
 * Spring Data JPA repository for the Feedback entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("select feedback from Feedback feedback where feedback.user.login = ?#{authentication.name}")
    List<Feedback> findByUserIsCurrentUser();

    @Query(
        """
            SELECT f FROM Feedback f
            WHERE (:status IS NULL OR f.feedbackStatus = :status)
              AND (:type IS NULL OR f.feedbackType = :type)
              AND (:tripId IS NULL OR f.trip.id = :tripId)
        """
    )
    Page<Feedback> findAllWithFilters(
        @Param("status") FeedbackStatus status,
        @Param("type") FeedbackType type,
        @Param("tripId") Long tripId,
        Pageable pageable
    );

    Optional<Feedback> findByFeedbackID(UUID feedbackID);

    List<Feedback> findByDriver_DriverIDAndFeedbackType(UUID driverId, FeedbackType type);

    boolean existsByTrip_TripIDAndUser_IdAndFeedbackType(UUID tripId, Long userId, FeedbackType feedbackType);

    List<Feedback> findByTrip_TripID(UUID tripId);
}
