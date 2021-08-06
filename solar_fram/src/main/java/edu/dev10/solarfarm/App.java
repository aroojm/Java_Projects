package edu.dev10.solarfarm;

import edu.dev10.solarfarm.data.PanelFileRepository;
import edu.dev10.solarfarm.domain.PanelService;
import edu.dev10.solarfarm.ui.ConsoleIO;
import edu.dev10.solarfarm.ui.FarmController;
import edu.dev10.solarfarm.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

@ComponentScan
@PropertySource("classpath:data.properties")
public class App {
    public static void main(String[] args) {
//        ConsoleIO console = new ConsoleIO();
//        View view = new View(console);
//
//        PanelFileRepository repository = new PanelFileRepository("data/panels.csv");
//        PanelService service = new PanelService(repository);
//
//        FarmController controller = new FarmController(service, view);
//        controller.run();

//        ApplicationContext container = new ClassPathXmlApplicationContext("dependency-configuration.xml");
//        FarmController controller = container.getBean(FarmController.class);
//        controller.run();

        ApplicationContext container = new AnnotationConfigApplicationContext(App.class);
        FarmController farmController = container.getBean(FarmController.class);
        farmController.run();




    }
}
