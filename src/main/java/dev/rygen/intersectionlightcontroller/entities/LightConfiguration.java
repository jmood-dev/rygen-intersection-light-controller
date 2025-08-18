package dev.rygen.intersectionlightcontroller.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.util.Objects;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "light_configuration")
public class LightConfiguration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "light_configuration_id")
    private int lightConfigurationId;

    @Column(name = "red_seconds")
    private int redSeconds;

    @Column(name = "yellow_seconds")
    private int yellowSeconds;

    @Column(name = "green_seconds")
    private int greenSeconds;

    @Column(name = "is_synchronized")
    private boolean isSynchronized;

    public int secondsForColor(LightColor lightColor) {
        switch (lightColor) {
            case RED:
                return redSeconds;
            case YELLOW:
                return yellowSeconds;
            case GREEN:
                return greenSeconds;
        }
        return -1; //if this is reached, there was an error
    }

    public void setSecondsForColor(int seconds, LightColor lightColor) {
        switch (lightColor) {
            case RED:
                redSeconds = seconds;
                break;
            case YELLOW:
                yellowSeconds = seconds;
                break;
            case GREEN:
                greenSeconds = seconds;
                break;
        }
    }
}
