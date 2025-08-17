package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.repositories.LightRepository;
import org.springframework.stereotype.Service;


@Service
public class LightService {

    private final LightRepository lightRepository;

    public LightService(LightRepository roadRepository) {
        this.lightRepository = roadRepository;
    }

    public Light createLight(Light light) {
        return this.lightRepository.save(light);
    }
}
