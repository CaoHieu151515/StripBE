package strip.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.RequestTrip;
import strip.repository.RequestTripRepository;
import strip.service.dto.RequestTripDTO;
import strip.service.mapper.RequestTripMapper;

/**
 * Service Implementation for managing {@link strip.domain.RequestTrip}.
 */
@Service
@Transactional
public class RequestTripService {

    private static final Logger LOG = LoggerFactory.getLogger(RequestTripService.class);

    private final RequestTripRepository requestTripRepository;

    private final RequestTripMapper requestTripMapper;

    public RequestTripService(RequestTripRepository requestTripRepository, RequestTripMapper requestTripMapper) {
        this.requestTripRepository = requestTripRepository;
        this.requestTripMapper = requestTripMapper;
    }

    /**
     * Save a requestTrip.
     *
     * @param requestTripDTO the entity to save.
     * @return the persisted entity.
     */
    public RequestTripDTO save(RequestTripDTO requestTripDTO) {
        LOG.debug("Request to save RequestTrip : {}", requestTripDTO);
        RequestTrip requestTrip = requestTripMapper.toEntity(requestTripDTO);
        requestTrip = requestTripRepository.save(requestTrip);
        return requestTripMapper.toDto(requestTrip);
    }

    /**
     * Update a requestTrip.
     *
     * @param requestTripDTO the entity to save.
     * @return the persisted entity.
     */
    public RequestTripDTO update(RequestTripDTO requestTripDTO) {
        LOG.debug("Request to update RequestTrip : {}", requestTripDTO);
        RequestTrip requestTrip = requestTripMapper.toEntity(requestTripDTO);
        requestTrip = requestTripRepository.save(requestTrip);
        return requestTripMapper.toDto(requestTrip);
    }

    /**
     * Partially update a requestTrip.
     *
     * @param requestTripDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RequestTripDTO> partialUpdate(RequestTripDTO requestTripDTO) {
        LOG.debug("Request to partially update RequestTrip : {}", requestTripDTO);

        return requestTripRepository
            .findById(requestTripDTO.getId())
            .map(existingRequestTrip -> {
                requestTripMapper.partialUpdate(existingRequestTrip, requestTripDTO);

                return existingRequestTrip;
            })
            .map(requestTripRepository::save)
            .map(requestTripMapper::toDto);
    }

    /**
     * Get all the requestTrips.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RequestTripDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all RequestTrips");
        return requestTripRepository.findAll(pageable).map(requestTripMapper::toDto);
    }

    /**
     * Get one requestTrip by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RequestTripDTO> findOne(Long id) {
        LOG.debug("Request to get RequestTrip : {}", id);
        return requestTripRepository.findById(id).map(requestTripMapper::toDto);
    }

    /**
     * Delete the requestTrip by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RequestTrip : {}", id);
        requestTripRepository.deleteById(id);
    }
}
