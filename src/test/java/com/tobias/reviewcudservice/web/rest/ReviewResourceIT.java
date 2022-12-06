package com.tobias.reviewcudservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tobias.reviewcudservice.IntegrationTest;
import com.tobias.reviewcudservice.domain.Review;
import com.tobias.reviewcudservice.repository.ReviewRepository;
import com.tobias.reviewcudservice.service.dto.ReviewDTO;
import com.tobias.reviewcudservice.service.mapper.ReviewMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReviewResourceIT {

    private static final String DEFAULT_IMG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_IMG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USERID = "AAAAAAAAAA";
    private static final String UPDATED_USERID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_WEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_HEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_HEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_CATALOGID = "AAAAAAAAAA";
    private static final String UPDATED_CATALOGID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private MockMvc restReviewMockMvc;

    private Review review;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity() {
        Review review = new Review()
            .imgName(DEFAULT_IMG_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .userid(DEFAULT_USERID)
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .catalogid(DEFAULT_CATALOGID);
        return review;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity() {
        Review review = new Review()
            .imgName(UPDATED_IMG_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .userid(UPDATED_USERID)
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .catalogid(UPDATED_CATALOGID);
        return review;
    }

    @BeforeEach
    public void initTest() {
        reviewRepository.deleteAll();
        review = createEntity();
    }

    @Test
    void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();
        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getImgName()).isEqualTo(DEFAULT_IMG_NAME);
        assertThat(testReview.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReview.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReview.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testReview.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testReview.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testReview.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testReview.getCatalogid()).isEqualTo(DEFAULT_CATALOGID);
    }

    @Test
    void createReviewWithExistingId() throws Exception {
        // Create the Review with an existing ID
        review.setId("existing_id");
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        // Get all the reviewList
        restReviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId())))
            .andExpect(jsonPath("$.[*].imgName").value(hasItem(DEFAULT_IMG_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].catalogid").value(hasItem(DEFAULT_CATALOGID)));
    }

    @Test
    void getReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        // Get the review
        restReviewMockMvc
            .perform(get(ENTITY_API_URL_ID, review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId()))
            .andExpect(jsonPath("$.imgName").value(DEFAULT_IMG_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.catalogid").value(DEFAULT_CATALOGID));
    }

    @Test
    void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).get();
        updatedReview
            .imgName(UPDATED_IMG_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .userid(UPDATED_USERID)
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .catalogid(UPDATED_CATALOGID);
        ReviewDTO reviewDTO = reviewMapper.toDto(updatedReview);

        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getImgName()).isEqualTo(UPDATED_IMG_NAME);
        assertThat(testReview.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReview.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReview.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testReview.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReview.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testReview.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testReview.getCatalogid()).isEqualTo(UPDATED_CATALOGID);
    }

    @Test
    void putNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID().toString());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID().toString());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID().toString());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reviewDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview.createdAt(UPDATED_CREATED_AT).userid(UPDATED_USERID).name(UPDATED_NAME);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getImgName()).isEqualTo(DEFAULT_IMG_NAME);
        assertThat(testReview.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReview.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReview.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testReview.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReview.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testReview.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testReview.getCatalogid()).isEqualTo(DEFAULT_CATALOGID);
    }

    @Test
    void fullUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview
            .imgName(UPDATED_IMG_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .userid(UPDATED_USERID)
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .catalogid(UPDATED_CATALOGID);

        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            )
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getImgName()).isEqualTo(UPDATED_IMG_NAME);
        assertThat(testReview.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReview.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReview.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testReview.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testReview.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testReview.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testReview.getCatalogid()).isEqualTo(UPDATED_CATALOGID);
    }

    @Test
    void patchNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID().toString());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reviewDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID().toString());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();
        review.setId(UUID.randomUUID().toString());

        // Create the Review
        ReviewDTO reviewDTO = reviewMapper.toDto(review);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReviewMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reviewDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review);

        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Delete the review
        restReviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, review.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
