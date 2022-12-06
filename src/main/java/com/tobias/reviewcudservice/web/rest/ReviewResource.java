package com.tobias.reviewcudservice.web.rest;

import com.tobias.reviewcudservice.repository.ReviewRepository;
import com.tobias.reviewcudservice.service.ReviewService;
import com.tobias.reviewcudservice.service.dto.ReviewDTO;
import com.tobias.reviewcudservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tobias.reviewcudservice.domain.Review}.
 */
@RestController
@RequestMapping("/api")
public class ReviewResource {

    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    private static final String ENTITY_NAME = "reviewCudServiceReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    public ReviewResource(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) throws URISyntaxException {
        log.debug("REST request to save Review : {}", reviewDTO);
        if (reviewDTO.getId() != null) throw new BadRequestAlertException(
            "A new review cannot already have an ID",
            ENTITY_NAME,
            "idexists"
        );

        ReviewDTO result = reviewService.save(reviewDTO);
        return ResponseEntity
            .created(new URI("/api/reviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> updateReview(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ReviewDTO reviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Review : {}, {}", id, reviewDTO);
        if (reviewDTO.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!Objects.equals(id, reviewDTO.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!reviewRepository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");

        ReviewDTO result = reviewService.update(reviewDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reviewDTO.getId()))
            .body(result);
    }

    @PatchMapping(value = "/reviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewDTO> partialUpdateReview(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ReviewDTO reviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Review partially : {}, {}", id, reviewDTO);
        if (reviewDTO.getId() == null) throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        if (!Objects.equals(id, reviewDTO.getId())) throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        if (!reviewRepository.existsById(id)) throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");

        Optional<ReviewDTO> result = reviewService.partialUpdate(reviewDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reviewDTO.getId())
        );
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Reviews");
        Page<ReviewDTO> page = reviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable String id) {
        log.debug("REST request to get Review : {}", id);
        Optional<ReviewDTO> reviewDTO = reviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewDTO);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        log.debug("REST request to delete Review : {}", id);
        reviewService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
