package com.sjsu.miaas.repository;

import java.util.List;

import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Request entity.
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
	
	@Query("select r from Request r where r.user_login = ?1")
    List<Request> getRequestsByUserId(String login_name);
	
}
