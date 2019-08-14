package core.mvc.tobe;

import core.annotation.web.Controller;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

  private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

  private static final Class<Controller> CONTROLLER = Controller.class;
  private String basePackage;


  public ControllerScanner(String basePackage) {
    this.basePackage = basePackage;
  }

  public Map<Class, Object> getControllers() {
    Reflections reflections = new Reflections(basePackage);
    Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CONTROLLER);

    return instantiateControllers(typesAnnotatedWith);
  }

  private Map<Class, Object> instantiateControllers(Set<Class<?>> typesAnnotatedWith) {
    return typesAnnotatedWith.stream()
        .collect(Collectors.toMap(clazz -> clazz, clazz -> {
          try {
            return clazz.newInstance();
          } catch (InstantiationException e) {
            logger.error("error {}", e.getMessage());
          } catch (IllegalAccessException e) {
            logger.error("error {}", e.getMessage());
          }
          return null;
        }));
  }

}
