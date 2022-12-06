package com.tobias.reviewcudservice.service.mapper;

import com.tobias.reviewcudservice.domain.Review;
import com.tobias.reviewcudservice.service.dto.ReviewDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Review} and its DTO {@link ReviewDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReviewMapper extends EntityMapper<ReviewDTO, Review> {}
