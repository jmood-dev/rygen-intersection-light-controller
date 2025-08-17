package dev.rygen.intersectionlightcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.rygen.intersectionlightcontroller.controllers.IntersectionController;
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
		intersectionController.createIntersection(null);
		assertEquals(1, intersectionService.getIntersectionRepository().count());
	}

	@Test
	void createThreeIntersections() {
		IntersectionController intersectionController = new IntersectionController(intersectionService, roadService, lightService);
		intersectionController.createIntersection(null);
		assertEquals(1, intersectionService.getIntersectionRepository().count());
		intersectionController.createIntersection(null);
		assertEquals(2, intersectionService.getIntersectionRepository().count());
		intersectionController.createIntersection(null);
		assertEquals(3, intersectionService.getIntersectionRepository().count());
	}

}
