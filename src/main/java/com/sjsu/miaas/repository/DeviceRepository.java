package com.sjsu.miaas.repository;

import com.sjsu.miaas.domain.Device;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Device entity.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {

}
