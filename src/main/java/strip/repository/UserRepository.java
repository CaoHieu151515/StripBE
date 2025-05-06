package strip.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import strip.domain.User;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query(
        "SELECT FUNCTION('DATE_FORMAT', u.createdDate, '%Y-%m-%d') as date, COUNT(u) " +
        "FROM User u " +
        "WHERE u.createdDate BETWEEN :from AND :to " +
        "AND EXISTS (SELECT 1 FROM u.authorities auth WHERE auth.name = :role) " +
        "GROUP BY date " +
        "ORDER BY date"
    )
    List<Object[]> countUserRegistrationsByDay(@Param("from") Instant from, @Param("to") Instant to, @Param("role") String role);

    @Query(
        "SELECT FUNCTION('MONTH', u.createdDate) as month, COUNT(u) " +
        "FROM User u " +
        "WHERE u.createdDate BETWEEN :from AND :to " +
        "AND EXISTS (SELECT 1 FROM u.authorities auth WHERE auth.name = :role) " +
        "GROUP BY month " +
        "ORDER BY month"
    )
    List<Object[]> countUserRegistrationsByMonth(@Param("from") Instant from, @Param("to") Instant to, @Param("role") String role);
}
