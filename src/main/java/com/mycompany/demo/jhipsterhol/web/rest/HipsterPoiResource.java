package com.mycompany.demo.jhipsterhol.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.demo.jhipsterhol.domain.HipsterPoi;
import com.mycompany.demo.jhipsterhol.repository.HipsterPoiRepository;
import com.mycompany.demo.jhipsterhol.web.rest.util.HeaderUtil;
import com.mycompany.demo.jhipsterhol.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing HipsterPoi.
 */
@RestController
@RequestMapping("/api")
public class HipsterPoiResource {

    private final Logger log = LoggerFactory.getLogger(HipsterPoiResource.class);
        
    @Inject
    private HipsterPoiRepository hipsterPoiRepository;
    
    /**
     * POST  /hipsterPois -> Create a new hipsterPoi.
     */
    @RequestMapping(value = "/hipsterPois",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HipsterPoi> createHipsterPoi(@Valid @RequestBody HipsterPoi hipsterPoi) throws URISyntaxException {
        log.debug("REST request to save HipsterPoi : {}", hipsterPoi);
        if (hipsterPoi.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("hipsterPoi", "idexists", "A new hipsterPoi cannot already have an ID")).body(null);
        }
        HipsterPoi result = hipsterPoiRepository.save(hipsterPoi);
        return ResponseEntity.created(new URI("/api/hipsterPois/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("hipsterPoi", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hipsterPois -> Updates an existing hipsterPoi.
     */
    @RequestMapping(value = "/hipsterPois",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HipsterPoi> updateHipsterPoi(@Valid @RequestBody HipsterPoi hipsterPoi) throws URISyntaxException {
        log.debug("REST request to update HipsterPoi : {}", hipsterPoi);
        if (hipsterPoi.getId() == null) {
            return createHipsterPoi(hipsterPoi);
        }
        HipsterPoi result = hipsterPoiRepository.save(hipsterPoi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("hipsterPoi", hipsterPoi.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hipsterPois -> get all the hipsterPois.
     */
    @RequestMapping(value = "/hipsterPois",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<HipsterPoi>> getAllHipsterPois(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of HipsterPois");
        Page<HipsterPoi> page = hipsterPoiRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hipsterPois");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hipsterPois/:id -> get the "id" hipsterPoi.
     */
    @RequestMapping(value = "/hipsterPois/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HipsterPoi> getHipsterPoi(@PathVariable Long id) {
        log.debug("REST request to get HipsterPoi : {}", id);
        HipsterPoi hipsterPoi = hipsterPoiRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(hipsterPoi)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /hipsterPois/:id -> delete the "id" hipsterPoi.
     */
    @RequestMapping(value = "/hipsterPois/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHipsterPoi(@PathVariable Long id) {
        log.debug("REST request to delete HipsterPoi : {}", id);
        hipsterPoiRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("hipsterPoi", id.toString())).build();
    }
}
