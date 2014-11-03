package com.sjsu.miaas.repository;

import java.util.List;

import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.domain.Request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the AmazonInstance entity.
 */
public interface AmazonInstanceRepository extends JpaRepository<AmazonInstance, Long> {
	
	@Query("select a from AmazonInstance a where a.instanceId = ?1")
    AmazonInstance getAmazonInstancebyId(String instance_id);
	
}
