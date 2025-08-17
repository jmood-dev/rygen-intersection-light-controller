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
@Table(name = "intersection")
public class Intersection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intersection_id")
    private int intersectionId;

    @Column(name = "roads")
    @OneToMany(fetch = FetchType.EAGER)
    private List<Road> roads;

    @Column(name = "active")
    private boolean active;

    public void tick(LightService lightService) {
        for (Road road : this.roads) {
            road.tick(lightService);
        }
    }

    public void setLightsActive(boolean isActive, LightService lightService) {
        for (Road road : this.roads) {
            for (Light light : road.getLights()) {
                light.setActive(isActive);
                lightService.getLightRepository().save(light);
            }
        }
    }

    public String lightsString() {
        String str = "";
        for (Road road : roads) {
            str += "[";
            for (Light light : road.getLights()) {
                str += "(";
                str += light.isActive() ? light.getLightColor().name() : "off";
                str += ")";
            }
            str += "]";
        }
        return str;
    }
}
