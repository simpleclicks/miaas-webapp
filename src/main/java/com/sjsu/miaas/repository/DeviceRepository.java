package com.sjsu.miaas.repository;

import java.math.BigInteger;
import java.util.List;

import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.domain.Device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Device entity.
 */
public interface DeviceRepository extends JpaRepository<Device, Long> {
	
	@Query("select d from Device d where d.request_id = ?1")
    List<Device> getDevicesByRequestId(BigInteger requestId);
	
	@Query("select d from Device d where d.deviceId = ?1")
    Device getDevicesByDeviceId(String deviceId);
}
