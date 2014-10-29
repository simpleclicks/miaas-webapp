package com.sjsu.miaas.repository;

import com.sjsu.miaas.domain.Request;
        import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Request entity.
 */
public interface RequestRepository extends JpaRepository<Request, Long> {

}
