package next.reflection;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {

  private static final String METHOD_PREFIX ="test";

  @Test
  public void run() throws Exception {
    Class<Junit3Test> clazz = Junit3Test.class;

    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getName().startsWith(METHOD_PREFIX)) {
        method.invoke(clazz.newInstance());
      }
    }
  }
}
