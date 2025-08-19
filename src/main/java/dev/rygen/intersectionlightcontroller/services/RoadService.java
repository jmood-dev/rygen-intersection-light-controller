package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.repositories.RoadRepository;
import org.springframework.stereotype.Service;


@Service
public class RoadService {

    private final RoadRepository roadRepository;

    public RoadService(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
    }

    public Road createRoad(Road road) {
        return this.roadRepository.save(road);
    }

    public RoadRepository getRoadRepository() {
        return this.roadRepository;
    }
}
