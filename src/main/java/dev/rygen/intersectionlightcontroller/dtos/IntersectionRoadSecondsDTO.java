package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import lombok.Builder;

@Builder
public record IntersectionRoadSecondsDTO(
        int intersectionId,
        int roadId,
        LightColor lightColor,
        int seconds
) {}
