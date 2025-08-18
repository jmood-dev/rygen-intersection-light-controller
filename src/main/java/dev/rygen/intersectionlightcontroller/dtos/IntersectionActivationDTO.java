package dev.rygen.intersectionlightcontroller.dtos;

import lombok.Builder;

@Builder
public record IntersectionActivationDTO(
        int intersectionId,
        boolean isActive
) {}
