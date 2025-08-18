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

import dev.rygen.intersectionlightcontroller.enums.LightColor;
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
        ensureOnlyOneRoadNotRed(lightService);
        int initialNumOfRoadNotRed = numOfRoadNotRed();

        for (Road road : this.roads) {
            road.tick(lightService);
        }

        ensureOnlyOneRoadNotRed(lightService);
        int finalNumOfRoadNotRed = numOfRoadNotRed();

        //both roads are red
        if (finalNumOfRoadNotRed == -1) {
            if (initialNumOfRoadNotRed > -1 && this.roads.get(1-initialNumOfRoadNotRed).hasSynchronizedLight()) {
                //if there is a road that just changed to red, set the other road to green
                this.roads.get(1-initialNumOfRoadNotRed).setLightsColor(LightColor.GREEN, lightService);
            } else {
                //if both roads started red, just set one of them to green
                for (int i = 0; i < this.roads.size(); i++) {
                    Road road = this.roads.get(i);
                    if (road.hasSynchronizedLight() && !road.hasGreenOrYellowLight()) {
                        road.setLightsColor(LightColor.GREEN, lightService);
                        break;
                    }
                }
            }
        }
    }

    private void ensureOnlyOneRoadNotRed(LightService lightService) {
        for (int i = 0; i < this.roads.size(); i++) {
            if (this.roads.get(i).hasSynchronizedLight() && this.roads.get(1-i).hasGreenOrYellowLight()) {
                this.roads.get(i).setLightsColor(LightColor.RED, lightService);
            }
        }
    }

    private int numOfRoadNotRed() {
        for (int i = 0; i < this.roads.size(); i++) {
            if (this.roads.get(i).hasGreenOrYellowLight()) {
                return i;
            }
        }
        return -1;
    }

    public void setLightsActive(boolean isActive, LightService lightService) {
        for (Road road : this.roads) {
            for (Light light : road.getLights()) {
                light.setActive(isActive);
                lightService.getLightRepository().save(light);
            }
        }
    }

    public LightConfiguration getLightConfigurationForRoadAndLight(int roadNum, int lightNum) {
        return this.roads.get(roadNum).getLights().get(lightNum).getLightConfiguration();
    }

    public void setLightConfigurationForRoadAndLight(LightConfiguration lightConfiguration, int roadNum, int lightNum, LightService lightService) {
        Light light = this.roads.get(roadNum).getLights().get(lightNum);
        light.setLightConfiguration(lightConfiguration);
        lightService.getLightRepository().save(light);
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
