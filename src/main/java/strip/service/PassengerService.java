package strip.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.Passenger;
import strip.repository.PassengerRepository;
import strip.service.dto.PassengerDTO;
import strip.service.mapper.PassengerMapper;

/**
 * Service Implementation for managing {@link strip.domain.Passenger}.
 */
@Service
@Transactional
public class PassengerService {

    private final Logger log = LoggerFactory.getLogger(PassengerService.class);

    private final PassengerRepository passengerRepository;

    private final PassengerMapper passengerMapper;

    public PassengerService(PassengerRepository passengerRepository, PassengerMapper passengerMapper) {
        this.passengerRepository = passengerRepository;
        this.passengerMapper = passengerMapper;
    }

    /**
     * Save a passenger.
     *
     * @param passengerDTO the entity to save.
     * @return the persisted entity.
     */
    public PassengerDTO save(PassengerDTO passengerDTO) {
        log.debug("Request to save Passenger : {}", passengerDTO);
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger = passengerRepository.save(passenger);
        return passengerMapper.toDto(passenger);
    }

    /**
     * Update a passenger.
     *
     * @param passengerDTO the entity to save.
     * @return the persisted entity.
     */
    public PassengerDTO update(PassengerDTO passengerDTO) {
        log.debug("Request to update Passenger : {}", passengerDTO);
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger = passengerRepository.save(passenger);
        return passengerMapper.toDto(passenger);
    }

    /**
     * Partially update a passenger.
     *
     * @param passengerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PassengerDTO> partialUpdate(PassengerDTO passengerDTO) {
        log.debug("Request to partially update Passenger : {}", passengerDTO);

        return passengerRepository
            .findById(passengerDTO.getId())
            .map(existingPassenger -> {
                passengerMapper.partialUpdate(existingPassenger, passengerDTO);

                return existingPassenger;
            })
            .map(passengerRepository::save)
            .map(passengerMapper::toDto);
    }

    /**
     * Get all the passengers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PassengerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Passengers");
        return passengerRepository.findAll(pageable).map(passengerMapper::toDto);
    }

    /**
     * Get one passenger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PassengerDTO> findOne(Long id) {
        log.debug("Request to get Passenger : {}", id);
        return passengerRepository.findById(id).map(passengerMapper::toDto);
    }

    /**
     * Delete the passenger by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Passenger : {}", id);
        passengerRepository.deleteById(id);
    }
}
