package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit3TestRunner {

  private static final String METHOD_PREFIX = "test";

  @Test
  public void run() throws Exception {
    Class<Junit3Test> clazz = Junit3Test.class;

    for (Method method : clazz.getDeclaredMethods()) {
      testMethodRun(clazz, method);
    }
  }

  private void testMethodRun(Class<Junit3Test> clazz, Method method)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    if (isTestMethod(method)) {
      method.invoke(clazz.newInstance());
    }
  }

  private boolean isTestMethod(Method method) {
    return method.getName().startsWith(METHOD_PREFIX);
  }
}
