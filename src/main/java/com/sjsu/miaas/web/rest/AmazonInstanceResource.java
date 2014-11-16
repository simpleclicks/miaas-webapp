package com.sjsu.miaas.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sjsu.miaas.AWSServices.AWSMetric;
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.repository.AmazonInstanceRepository;
import com.sjsu.miaas.service.AmazonInstanceService;

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
 * REST controller for managing AmazonInstance.
 */
@RestController
@RequestMapping("/app")
public class AmazonInstanceResource {

    private final Logger log = LoggerFactory.getLogger(AmazonInstanceResource.class);

    @Inject
    private AmazonInstanceRepository amazoninstanceRepository;
    
    @Inject
    private AmazonInstanceService amazinInstService;

    /**
     * POST  /rest/amazoninstances -> Create a new amazoninstance.
     */
    @RequestMapping(value = "/rest/amazoninstances",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody AmazonInstance amazoninstance) {
        log.debug("REST request to save AmazonInstance : {}", amazoninstance);
        amazoninstanceRepository.save(amazoninstance);
    }

    /**
     * GET  /rest/amazoninstances -> get all the amazoninstances.
     */
    @RequestMapping(value = "/rest/amazoninstances",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AmazonInstance> getAll() {
        log.debug("REST request to get all AmazonInstances");
        return amazoninstanceRepository.findAll();
    }

    /**
     * GET  /rest/amazoninstances/:id -> get the "id" amazoninstance.
     */
    @RequestMapping(value = "/rest/amazoninstances/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AmazonInstance> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get AmazonInstance : {}", id);
        AmazonInstance amazoninstance = amazoninstanceRepository.findOne(id);
        if (amazoninstance == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(amazoninstance, HttpStatus.OK);
    }
    
    /**
     * GET  /rest/amazoninstances/:id -> get the "id" amazoninstance.
     */
    @RequestMapping(value = "/rest/amazoninstances/monitor",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<AWSMetric>> getInstanceMetrics(HttpServletResponse response) {
        log.debug("REST request to get AmazonInstance metrics : {}");
        List<AWSMetric> awsMetrics = amazinInstService.getAmazonMetrics();
        if (awsMetrics == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(awsMetrics, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/amazoninstances/:id -> delete the "id" amazoninstance.
     */
    @RequestMapping(value = "/rest/amazoninstances/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete AmazonInstance : {}", id);
        amazoninstanceRepository.delete(id);
    }
}
