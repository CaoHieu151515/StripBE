package strip.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import strip.domain.PackageDriver;
import strip.repository.PackageDriverRepository;
import strip.service.dto.PackageDriverDTO;
import strip.service.mapper.PackageDriverMapper;

/**
 * Service Implementation for managing {@link strip.domain.PackageDriver}.
 */
@Service
@Transactional
public class PackageDriverService {

    private final Logger log = LoggerFactory.getLogger(PackageDriverService.class);

    private final PackageDriverRepository packageDriverRepository;

    private final PackageDriverMapper packageDriverMapper;

    public PackageDriverService(PackageDriverRepository packageDriverRepository, PackageDriverMapper packageDriverMapper) {
        this.packageDriverRepository = packageDriverRepository;
        this.packageDriverMapper = packageDriverMapper;
    }

    /**
     * Save a packageDriver.
     *
     * @param packageDriverDTO the entity to save.
     * @return the persisted entity.
     */
    public PackageDriverDTO save(PackageDriverDTO packageDriverDTO) {
        log.debug("Request to save PackageDriver : {}", packageDriverDTO);
        PackageDriver packageDriver = packageDriverMapper.toEntity(packageDriverDTO);
        packageDriver = packageDriverRepository.save(packageDriver);
        return packageDriverMapper.toDto(packageDriver);
    }

    /**
     * Update a packageDriver.
     *
     * @param packageDriverDTO the entity to save.
     * @return the persisted entity.
     */
    public PackageDriverDTO update(PackageDriverDTO packageDriverDTO) {
        log.debug("Request to update PackageDriver : {}", packageDriverDTO);
        PackageDriver packageDriver = packageDriverMapper.toEntity(packageDriverDTO);
        packageDriver = packageDriverRepository.save(packageDriver);
        return packageDriverMapper.toDto(packageDriver);
    }

    /**
     * Partially update a packageDriver.
     *
     * @param packageDriverDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PackageDriverDTO> partialUpdate(PackageDriverDTO packageDriverDTO) {
        log.debug("Request to partially update PackageDriver : {}", packageDriverDTO);

        return packageDriverRepository
            .findById(packageDriverDTO.getId())
            .map(existingPackageDriver -> {
                packageDriverMapper.partialUpdate(existingPackageDriver, packageDriverDTO);

                return existingPackageDriver;
            })
            .map(packageDriverRepository::save)
            .map(packageDriverMapper::toDto);
    }

    /**
     * Get all the packageDrivers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PackageDriverDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PackageDrivers");
        return packageDriverRepository.findAll(pageable).map(packageDriverMapper::toDto);
    }

    /**
     * Get one packageDriver by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PackageDriverDTO> findOne(Long id) {
        log.debug("Request to get PackageDriver : {}", id);
        return packageDriverRepository.findById(id).map(packageDriverMapper::toDto);
    }

    /**
     * Delete the packageDriver by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PackageDriver : {}", id);
        packageDriverRepository.deleteById(id);
    }
}
