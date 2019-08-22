package core.mvc.tobe;

import static java.util.stream.Collectors.toMap;

import core.annotation.web.Controller;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

  private static final Logger logger = LoggerFactory.getLogger(ControllerScanner.class);

  private static final Class<Controller> CONTROLLER = Controller.class;
  private Object[] basePackages;

  public ControllerScanner(Object[] basePackages) {
    this.basePackages = basePackages;
  }

  public Map<Class, Object> getControllers() {
    Reflections reflections = new Reflections(basePackages);
    Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(CONTROLLER);

    return instantiateControllers(typesAnnotatedWith);
  }

  private Map<Class, Object> instantiateControllers(Set<Class<?>> typesAnnotatedWith) {
    return typesAnnotatedWith.stream()
        .collect(
            toMap(clazz -> clazz, clazz -> getNewInstance(clazz)));
  }

  private Object getNewInstance(Class<?> clazz) {
    try {
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      logger.error("error {}", e.getMessage());
    }
    return null;
  }

}