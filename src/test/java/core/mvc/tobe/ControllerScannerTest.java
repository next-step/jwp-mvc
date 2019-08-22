package core.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import next.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerScannerTest {

  @Test
  @DisplayName("@Controller 를 찾는다")
  void scanControllers() {
    String basePackage = "next.controller";
    String basePackage1 = "core.mvc.tobe";
    Object [] basePackages ={basePackage,basePackage1};

    ControllerScanner controllerScanner = new ControllerScanner(basePackages);

    Map<Class, Object> controllers = controllerScanner.getControllers();
    assertThat(controllers).containsOnlyKeys(UserController.class, MyController.class);
  }
}