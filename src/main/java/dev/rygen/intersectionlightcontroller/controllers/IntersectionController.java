package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.RoadService;
import dev.rygen.intersectionlightcontroller.services.LightService;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import org.springframework.web.bind.annotation.*;

import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;

import java.util.List;
import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/intersections")
public class IntersectionController {

    private final IntersectionService intersectionService;
    private final RoadService roadService;
    private final LightService lightService;

    public IntersectionController(IntersectionService intersectionService, RoadService roadService, LightService lightService) {
        this.intersectionService = intersectionService;
        this.roadService = roadService;
        this.lightService = lightService;
    }

    @PostMapping
    public void createIntersection(@RequestBody IntersectionDTO intersectionDto) {
        List<Road> roads = makeRoads(makeLights(2, false, LightColor.RED), makeLights(2, false, LightColor.RED));
        Intersection intersection = Intersection.builder()
                .roads(roads)
                .build();
        this.intersectionService.createIntersection(intersection);
    }

    private List<Light> makeLights(int numLights, boolean active, LightColor color) {
        List<Light> lights = new ArrayList<Light>();
        for (int i = 0; i < numLights; i++) {
            Light light = Light.builder().active(active).lightColor(color).build();
            this.lightService.createLight(light);
            lights.add(light);
        }
        return lights;
    }

    private List<Road> makeRoads(List<Light>... lightLists) {
        List<Road> roads = new ArrayList<Road>();
        for (List<Light> lightList : lightLists) {
            Road road = Road.builder().lights(lightList).build();
            this.roadService.createRoad(road);
            roads.add(road);
        }
        return roads;
    }
}
