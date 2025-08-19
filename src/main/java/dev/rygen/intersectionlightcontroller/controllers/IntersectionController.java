package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionRoadSecondsDTO;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionActivationDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.RoadService;
import dev.rygen.intersectionlightcontroller.services.LightService;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.LightConfiguration;
import dev.rygen.intersectionlightcontroller.entities.Road;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import org.springframework.web.bind.annotation.*;

import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

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

    @GetMapping
    public List<Intersection> getIntersections() {
        return this.intersectionService.getIntersectionRepository().findAll();
    }

    @PostMapping
    public void createIntersection(@RequestBody IntersectionDTO intersectionDto) {
        createIntersection(true);
    }

    @PostMapping("/create")
    public Intersection createIntersection() {
        return createIntersection(true);
    }

    public Intersection createIntersection(boolean isSynchronized) {
        List<Road> roads = makeRoads(makeLights(2, true, LightColor.RED, isSynchronized), makeLights(2, true, LightColor.RED, isSynchronized));
        Intersection intersection = Intersection.builder()
                .roads(roads).active(true)
                .build();
        return this.intersectionService.createIntersection(intersection);
    }

    @PostMapping("/setActive")
    public void setActive(@RequestBody IntersectionActivationDTO intersectionActivationDTO) {
        int id = intersectionActivationDTO.intersectionId();
        Optional<Intersection> intersectionOpt = this.intersectionService.getIntersectionRepository().findById(id);
        intersectionOpt.ifPresent(intersection -> {
            intersection.setActive(intersectionActivationDTO.isActive());
            intersection.setLightsActive(intersectionActivationDTO.isActive(), lightService);
            intersectionService.getIntersectionRepository().save(intersection);
        });
    }

    @PostMapping("/setSecondsForRoadAndColor")
    public void setSeconds(@RequestBody IntersectionRoadSecondsDTO intersectionRoadSecondsDTO) {
        Optional<Intersection> intersectionOpt = this.intersectionService.getIntersectionRepository().findById(intersectionRoadSecondsDTO.intersectionId());
        if (intersectionOpt.isPresent()) {
            Intersection intersection = intersectionOpt.get();
            for (int roadNum = 0; roadNum < intersection.getRoads().size(); roadNum++) {
                Road road = intersection.getRoads().get(roadNum);
                if (road.getRoadId() == intersectionRoadSecondsDTO.roadId()) {
                    if (intersectionRoadSecondsDTO.lightColor() == LightColor.GREEN)
                        road.setGreenSeconds(intersectionRoadSecondsDTO.seconds());
                    else if (intersectionRoadSecondsDTO.lightColor() == LightColor.YELLOW)
                        road.setYellowSeconds(intersectionRoadSecondsDTO.seconds());
                    for (int lightNum = 0; lightNum < road.getLights().size(); lightNum++) {
                        LightConfiguration lightConfiguration = intersection.getLightConfigurationForRoadAndLight(roadNum, lightNum);
                        lightConfiguration.setSecondsForColor(intersectionRoadSecondsDTO.seconds(), intersectionRoadSecondsDTO.lightColor());
                        intersection.setLightConfigurationForRoadAndLight(lightConfiguration, roadNum, lightNum, this.lightService);
                    }
                    this.roadService.getRoadRepository().save(road);
                }
            }
        }
    }

    private List<Light> makeLights(int numLights, boolean active, LightColor color, boolean isSynchronized) {
        List<Light> lights = new ArrayList<Light>();
        for (int i = 0; i < numLights; i++) {
            LightConfiguration lightConfiguration = LightConfiguration.builder().redSeconds(5).yellowSeconds(2).greenSeconds(3).isSynchronized(isSynchronized).build();
            Light light = Light.builder().active(active).lightColor(color).lightConfiguration(lightConfiguration).build();
            this.lightService.createLight(light);
            lights.add(light);
        }
        return lights;
    }

    private List<Road> makeRoads(List<Light>... lightLists) {
        List<Road> roads = new ArrayList<Road>();
        for (List<Light> lightList : lightLists) {
            Road road = Road.builder().lights(lightList).greenSeconds(lightList.get(0).getLightConfiguration().getGreenSeconds())
                .yellowSeconds(lightList.get(0).getLightConfiguration().getYellowSeconds()).build();
            this.roadService.createRoad(road);
            roads.add(road);
        }
        return roads;
    }
}
