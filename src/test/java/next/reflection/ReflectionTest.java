package next.reflection;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionTest {

  private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

  @Test
  public void showClass() {
    Class<Question> clazz = Question.class;
    logger.debug(clazz.getName());

    Arrays.stream(clazz.getDeclaredFields())
        .forEach(field -> logger.debug("field : {} ", field.toString()));

    Arrays.stream(clazz.getDeclaredConstructors())
        .forEach(constructor -> logger.debug("constructor : {}", constructor.toString()));

    Arrays.stream(clazz.getMethods())
        .forEach(method -> logger.debug("method : {}", method.toString()));
  }

}
