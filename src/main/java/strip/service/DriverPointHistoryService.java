package strip.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.DriverPointHistory;
import strip.repository.DriverPointHistoryRepository;

/**
 * Service Implementation for managing {@link strip.domain.DriverPointHistory}.
 */
@Service
@Transactional
public class DriverPointHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(DriverPointHistoryService.class);

    private final DriverPointHistoryRepository driverPointHistoryRepository;

    public DriverPointHistoryService(DriverPointHistoryRepository driverPointHistoryRepository) {
        this.driverPointHistoryRepository = driverPointHistoryRepository;
    }

    /**
     * Save a driverPointHistory.
     *
     * @param driverPointHistory the entity to save.
     * @return the persisted entity.
     */
    public DriverPointHistory save(DriverPointHistory driverPointHistory) {
        LOG.debug("Request to save DriverPointHistory : {}", driverPointHistory);
        return driverPointHistoryRepository.save(driverPointHistory);
    }

    /**
     * Update a driverPointHistory.
     *
     * @param driverPointHistory the entity to save.
     * @return the persisted entity.
     */
    public DriverPointHistory update(DriverPointHistory driverPointHistory) {
        LOG.debug("Request to update DriverPointHistory : {}", driverPointHistory);
        return driverPointHistoryRepository.save(driverPointHistory);
    }

    /**
     * Partially update a driverPointHistory.
     *
     * @param driverPointHistory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DriverPointHistory> partialUpdate(DriverPointHistory driverPointHistory) {
        LOG.debug("Request to partially update DriverPointHistory : {}", driverPointHistory);

        return driverPointHistoryRepository
            .findById(driverPointHistory.getId())
            .map(existingDriverPointHistory -> {
                if (driverPointHistory.getPointId() != null) {
                    existingDriverPointHistory.setPointId(driverPointHistory.getPointId());
                }
                if (driverPointHistory.getPoint() != null) {
                    existingDriverPointHistory.setPoint(driverPointHistory.getPoint());
                }
                if (driverPointHistory.getReason() != null) {
                    existingDriverPointHistory.setReason(driverPointHistory.getReason());
                }
                if (driverPointHistory.getDate() != null) {
                    existingDriverPointHistory.setDate(driverPointHistory.getDate());
                }
                if (driverPointHistory.getStatus() != null) {
                    existingDriverPointHistory.setStatus(driverPointHistory.getStatus());
                }

                return existingDriverPointHistory;
            })
            .map(driverPointHistoryRepository::save);
    }

    /**
     * Get all the driverPointHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DriverPointHistory> findAll(Pageable pageable) {
        LOG.debug("Request to get all DriverPointHistories");
        return driverPointHistoryRepository.findAll(pageable);
    }

    /**
     * Get one driverPointHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DriverPointHistory> findOne(Long id) {
        LOG.debug("Request to get DriverPointHistory : {}", id);
        return driverPointHistoryRepository.findById(id);
    }

    /**
     * Delete the driverPointHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DriverPointHistory : {}", id);
        driverPointHistoryRepository.deleteById(id);
    }
}
