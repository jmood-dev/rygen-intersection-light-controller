package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Objects;
import java.util.Map;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.services.LightService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "light")
public class Light {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "light_id")
    private int lightId;

    @Column(name = "active")
    private boolean active;

    @Column(name = "light_color")
    private LightColor lightColor;

    @Column(name = "light_configuration")
    private LightConfiguration lightConfiguration;

    @Column(name = "seconds_on_color")
    private int secondsOnColor;

    public void tick(LightService lightService) {
        //Lights that are not active should not tick
        //Synchronized red lights wait for the other road to turn red
        if (!active || lightConfiguration.isSynchronized() && lightColor == LightColor.RED)
            return;

        secondsOnColor++;

        if (secondsOnColor >= this.lightConfiguration.secondsForColor(lightColor)) {
            secondsOnColor = 0;
            advanceColor();
        }

        lightService.getLightRepository().save(this);
    }

    public void advanceColor() {
        this.lightColor = findNextColor(this.lightColor);
    }

    private static Map<LightColor, LightColor> cycleOrder = Map.of(
        LightColor.GREEN, LightColor.YELLOW,
        LightColor.YELLOW, LightColor.RED,
        LightColor.RED, LightColor.GREEN
    );
    
    private static LightColor findNextColor(LightColor lightColor) {
        return cycleOrder.get(lightColor);
    }
}
