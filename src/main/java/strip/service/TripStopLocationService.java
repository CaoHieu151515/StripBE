package strip.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.TripStopLocation;
import strip.repository.TripStopLocationRepository;
import strip.service.dto.TripStopLocationDTO;
import strip.service.mapper.TripStopLocationMapper;

/**
 * Service Implementation for managing {@link strip.domain.TripStopLocation}.
 */
@Service
@Transactional
public class TripStopLocationService {

    private final Logger log = LoggerFactory.getLogger(TripStopLocationService.class);

    private final TripStopLocationRepository tripStopLocationRepository;

    private final TripStopLocationMapper tripStopLocationMapper;

    public TripStopLocationService(TripStopLocationRepository tripStopLocationRepository, TripStopLocationMapper tripStopLocationMapper) {
        this.tripStopLocationRepository = tripStopLocationRepository;
        this.tripStopLocationMapper = tripStopLocationMapper;
    }

    /**
     * Save a tripStopLocation.
     *
     * @param tripStopLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public TripStopLocationDTO save(TripStopLocationDTO tripStopLocationDTO) {
        log.debug("Request to save TripStopLocation : {}", tripStopLocationDTO);
        TripStopLocation tripStopLocation = tripStopLocationMapper.toEntity(tripStopLocationDTO);
        tripStopLocation = tripStopLocationRepository.save(tripStopLocation);
        return tripStopLocationMapper.toDto(tripStopLocation);
    }

    /**
     * Update a tripStopLocation.
     *
     * @param tripStopLocationDTO the entity to save.
     * @return the persisted entity.
     */
    public TripStopLocationDTO update(TripStopLocationDTO tripStopLocationDTO) {
        log.debug("Request to update TripStopLocation : {}", tripStopLocationDTO);
        TripStopLocation tripStopLocation = tripStopLocationMapper.toEntity(tripStopLocationDTO);
        tripStopLocation = tripStopLocationRepository.save(tripStopLocation);
        return tripStopLocationMapper.toDto(tripStopLocation);
    }

    /**
     * Partially update a tripStopLocation.
     *
     * @param tripStopLocationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TripStopLocationDTO> partialUpdate(TripStopLocationDTO tripStopLocationDTO) {
        log.debug("Request to partially update TripStopLocation : {}", tripStopLocationDTO);

        return tripStopLocationRepository
            .findById(tripStopLocationDTO.getId())
            .map(existingTripStopLocation -> {
                tripStopLocationMapper.partialUpdate(existingTripStopLocation, tripStopLocationDTO);

                return existingTripStopLocation;
            })
            .map(tripStopLocationRepository::save)
            .map(tripStopLocationMapper::toDto);
    }

    /**
     * Get all the tripStopLocations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TripStopLocationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TripStopLocations");
        return tripStopLocationRepository.findAll(pageable).map(tripStopLocationMapper::toDto);
    }

    /**
     * Get one tripStopLocation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TripStopLocationDTO> findOne(Long id) {
        log.debug("Request to get TripStopLocation : {}", id);
        return tripStopLocationRepository.findById(id).map(tripStopLocationMapper::toDto);
    }

    /**
     * Delete the tripStopLocation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TripStopLocation : {}", id);
        tripStopLocationRepository.deleteById(id);
    }
}
