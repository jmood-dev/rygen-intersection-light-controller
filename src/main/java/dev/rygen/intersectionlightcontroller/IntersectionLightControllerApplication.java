package dev.rygen.intersectionlightcontroller;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import dev.rygen.intersectionlightcontroller.services.LightService;
import dev.rygen.intersectionlightcontroller.entities.Intersection;

@SpringBootApplication
public class IntersectionLightControllerApplication {

	private static IntersectionService intersectionService;
	private static LightService lightService;

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(IntersectionLightControllerApplication.class, args);
		intersectionService = applicationContext.getBean(IntersectionService.class);
		lightService = applicationContext.getBean(LightService.class);

		ActionListener intersectionTicks = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Intersection> intersections = intersectionService.getIntersectionRepository().findAll();
				for (Intersection intersection : intersections) {
					intersection.tick(lightService);
					System.out.println(intersection.lightsString());
				}
				System.out.println("===");
			}
		};

		Timer timer = new Timer(1000, intersectionTicks);
		timer.start();
	}

}
