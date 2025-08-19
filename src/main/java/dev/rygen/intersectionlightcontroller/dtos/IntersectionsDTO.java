package dev.rygen.intersectionlightcontroller.dtos;

import java.util.List;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import lombok.Builder;

@Builder
public record IntersectionsDTO(
        List<Intersection> intersections
) {}
