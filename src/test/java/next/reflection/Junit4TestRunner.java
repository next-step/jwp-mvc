package next.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

public class Junit4TestRunner {

  @Test
  public void run() throws Exception {
    Class<Junit4Test> clazz = Junit4Test.class;

    for (Method method : clazz.getDeclaredMethods()) {
      annotationPresentMethodInvoke(clazz, method);
    }
  }

  private void annotationPresentMethodInvoke(Class<Junit4Test> clazz, Method method)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    if (method.isAnnotationPresent(MyTest.class)) {
      method.invoke(clazz.newInstance());
    }
  }
}
