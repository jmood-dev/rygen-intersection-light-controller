package dev.rygen.intersectionlightcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.rygen.intersectionlightcontroller.controllers.IntersectionController;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.entities.LightConfiguration;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.LightService;
import dev.rygen.intersectionlightcontroller.services.RoadService;

@SpringBootTest
class IntersectionLightControllerApplicationTests {

	@Autowired
	private IntersectionService intersectionService;
	@Autowired
	private RoadService roadService;
	@Autowired
	private LightService lightService;

	@BeforeEach
	public void setUp() {
		intersectionService.getIntersectionRepository().deleteAll();
	}

	@Test
	void createIntersection() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(false);
		assertEquals(1, intersectionService.getIntersectionRepository().count());
	}

	@Test
	void createThreeIntersections() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(false);
		assertEquals(1, intersectionService.getIntersectionRepository().count());
		intersectionController.createIntersection(false);
		assertEquals(2, intersectionService.getIntersectionRepository().count());
		intersectionController.createIntersection(false);
		assertEquals(3, intersectionService.getIntersectionRepository().count());
	}

	@Test
	void lightManualCycle() {
		Light light = Light.builder().active(false).lightColor(LightColor.RED).build();
		this.lightService.createLight(light);

		assertEquals(LightColor.RED, light.getLightColor());
		light.advanceColor();
		assertEquals(LightColor.GREEN, light.getLightColor());
		light.advanceColor();
		assertEquals(LightColor.YELLOW, light.getLightColor());
		light.advanceColor();
		assertEquals(LightColor.RED, light.getLightColor());
		light.advanceColor();
		assertEquals(LightColor.GREEN, light.getLightColor());
		light.advanceColor();
		assertEquals(LightColor.YELLOW, light.getLightColor());
		light.advanceColor();
	}

	@Test
	void intersectionAutomaticCycle() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(false);
		for (int i = 0; i < 5; i++) {
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
		}
		for (int i = 0; i < 3; i++) {
			assertEquals(LightColor.GREEN, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
		}
		for (int i = 0; i < 2; i++) {
			assertEquals(LightColor.YELLOW, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
		}
		assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
	}

	@Test
	void intersectionActivation() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(false);
		assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
		intersectionService.getIntersectionRepository().findAll().get(0).setLightsActive(false, lightService);

		for (int i = 0; i < 6; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
		}

		intersectionService.getIntersectionRepository().findAll().get(0).setLightsActive(true, lightService);
		for (int i = 0; i < 5; i++) {
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
		}
		for (int i = 0; i < 3; i++) {
			assertEquals(LightColor.GREEN, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
		}
		for (int i = 0; i < 2; i++) {
			assertEquals(LightColor.YELLOW, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
		}
		assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
	}

	@Test
	void getAndSetLightConfiguration() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(false);
		LightConfiguration lightConfiguration00 = intersectionService.getIntersectionRepository().findAll().get(0).getLightConfigurationForRoadAndLight(0, 0);

		assertEquals(5, lightConfiguration00.getRedSeconds());
		assertEquals(2, lightConfiguration00.getYellowSeconds());
		assertEquals(3, lightConfiguration00.getGreenSeconds());

		LightConfiguration newLightConfiguration = LightConfiguration.builder().redSeconds(8).yellowSeconds(7).greenSeconds(6).build();
		intersectionService.getIntersectionRepository().findAll().get(0).setLightConfigurationForRoadAndLight(newLightConfiguration, 0, 0, lightService);

		lightConfiguration00 = intersectionService.getIntersectionRepository().findAll().get(0).getLightConfigurationForRoadAndLight(0, 0);

		assertEquals(8, lightConfiguration00.getRedSeconds());
		assertEquals(7, lightConfiguration00.getYellowSeconds());
		assertEquals(6, lightConfiguration00.getGreenSeconds());

		LightConfiguration lightConfiguration11 = intersectionService.getIntersectionRepository().findAll().get(0).getLightConfigurationForRoadAndLight(1, 1);

		assertEquals(5, lightConfiguration11.getRedSeconds());
		assertEquals(2, lightConfiguration11.getYellowSeconds());
		assertEquals(3, lightConfiguration11.getGreenSeconds());
	}

	@Test
	void synchronizedLights() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(null);

		assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
		assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		for (int i = 0; i < 3; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.GREEN, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		}
		for (int i = 0; i < 2; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.YELLOW, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		}
		for (int i = 0; i < 3; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			assertEquals(LightColor.GREEN, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		}
		for (int i = 0; i < 2; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			assertEquals(LightColor.YELLOW, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		}
		for (int i = 0; i < 3; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.GREEN, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		}
		for (int i = 0; i < 2; i++) {
			intersectionService.getIntersectionRepository().findAll().get(0).tick(lightService);
			assertEquals(LightColor.YELLOW, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(0).getLights().get(0).getLightColor());
			assertEquals(LightColor.RED, intersectionService.getIntersectionRepository().findAll().get(0).getRoads().get(1).getLights().get(0).getLightColor());
		}
	}
}
