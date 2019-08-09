package next.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
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

  @Test
  public void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
    Class<Student> clazz = Student.class;

    Student student = new Student();
    Field nameField = clazz.getDeclaredField("name");
    nameField.setAccessible(true);
    nameField.set(student, "changjun");

    Field ageField = clazz.getDeclaredField("age");
    ageField.setAccessible(true);
    ageField.set(student, 31);

    assertThat(student.getName()).isEqualTo("changjun");
    assertThat(student.getAge()).isEqualTo(31);
  }

}
