package com.sjsu.miaas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.RequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Request.
 */
@RestController
@RequestMapping("/app")
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    @Inject
    private RequestRepository requestRepository;

    /**
     * POST  /rest/requests -> Create a new request.
     */
    @RequestMapping(value = "/rest/requests",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Request request) {
        log.debug("REST request to save Request : {}", request);
        requestRepository.save(request);
    }

    /**
     * GET  /rest/requests -> get all the requests.
     */
    @RequestMapping(value = "/rest/requests",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Request> getAll() {
        log.debug("REST request to get all Requests");
        return requestRepository.findAll();
    }

    /**
     * GET  /rest/requests/:id -> get the "id" request.
     */
    @RequestMapping(value = "/rest/requests/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Request> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Request : {}", id);
        Request request = requestRepository.findOne(id);
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(request, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/requests/:id -> delete the "id" request.
     */
    @RequestMapping(value = "/rest/requests/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Request : {}", id);
        requestRepository.delete(id);
    }
}
