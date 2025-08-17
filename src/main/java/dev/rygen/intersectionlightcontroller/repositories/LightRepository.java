package dev.rygen.intersectionlightcontroller.repositories;

import dev.rygen.intersectionlightcontroller.entities.Light;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LightRepository extends JpaRepository<Light, Integer> {
}
