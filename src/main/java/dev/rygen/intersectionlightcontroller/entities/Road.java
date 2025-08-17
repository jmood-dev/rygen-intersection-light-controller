package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

import dev.rygen.intersectionlightcontroller.services.LightService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "road")
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "road_id")
    private int roadId;

    @Column(name = "lights")
    @OneToMany(fetch = FetchType.EAGER)
    private List<Light> lights;

    public void tick(LightService lightService) {
        for (Light light : this.lights) {
            light.tick(lightService);
        }
    }
}
