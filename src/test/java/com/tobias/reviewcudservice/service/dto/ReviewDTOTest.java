package com.tobias.reviewcudservice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.tobias.reviewcudservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewDTO.class);
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setId("id1");
        ReviewDTO reviewDTO2 = new ReviewDTO();
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO2.setId(reviewDTO1.getId());
        assertThat(reviewDTO1).isEqualTo(reviewDTO2);
        reviewDTO2.setId("id2");
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO1.setId(null);
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
    }
}
