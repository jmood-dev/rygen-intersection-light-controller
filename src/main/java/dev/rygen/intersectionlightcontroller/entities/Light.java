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

import dev.rygen.intersectionlightcontroller.enums.LightColor;
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
}
