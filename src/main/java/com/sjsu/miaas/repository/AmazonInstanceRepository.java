package com.sjsu.miaas.repository;

import com.sjsu.miaas.domain.AmazonInstance;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the AmazonInstance entity.
 */
public interface AmazonInstanceRepository extends JpaRepository<AmazonInstance, Long> {
	
}
