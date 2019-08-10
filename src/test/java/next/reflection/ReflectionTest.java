package next.reflection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @DisplayName("클래스의 구성요소 출력")
    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.info("fields \n" + StringUtils.join(clazz.getDeclaredFields(), "\n"));
        logger.info("Constructors \n" + StringUtils.join(clazz.getDeclaredConstructors(), "\n"));
        logger.info("Methods \n" + StringUtils.join(clazz.getDeclaredMethods(), "\n"));
    }

    @DisplayName("클래스 필드 접근")
    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Student> clazz = Student.class;
        Student student = (Student) clazz.getConstructors()[0].newInstance();

        Field name = clazz.getDeclaredField("name");
        name.setAccessible(true);
        name.set(student, "jun");

        Field age = clazz.getDeclaredField("age");
        age.setAccessible(true);
        age.set(student, 33);

        assertThat(student.getName()).isEqualTo("jun");
        assertThat(student.getAge()).isEqualTo(33);

        logger.info(student.toString());
    }

    @DisplayName("생성자 파라미터로 객체를 생성하는 방법")
    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Date expectedDate = new Date();
        Question expectedQuestion1 = new Question("writer", "title", "content");
        Object[] expectedArgument1 = new Object[] {
                "writer", "title", "content"
        };
        Question expectedQuestion2 = new Question(50, "writer", "title", "content", expectedDate, 100);
        Object[] expectedArgument2 = new Object[]{
                50, "writer", "title", "content", expectedDate, 100
        };

        List<Object[]> expectedArguments = asList(expectedArgument1, expectedArgument2);
        List<Holder> expectedResults = asList(new Holder<>(expectedQuestion1, expectedArgument1), new Holder<>(expectedQuestion2, expectedArgument2));

        List<Holder<Question>> results = new ArrayList<>();

        Constructor[] constructors = clazz.getConstructors();

        for (Constructor constructor : constructors) {
            for (Object[] expectedArgument : expectedArguments) {
                if (constructor.getParameterCount() == expectedArgument.length) {
                    results.add(new Holder(constructor.newInstance(expectedArgument), expectedArgument));
                }
            }
        }

        results.stream()
                .peek(result -> assertContainsHolder(result, expectedResults))
                .map(Object::toString)
                .forEach(logger::info);
    }

    private void assertContainsHolder(Holder holder, List<Holder> expectedHolders) {
        assertTrue(expectedHolders.contains(holder));
    }

    private static class Holder<T> {
        private T object;
        private Object[] expectedArguments;

        public Holder(T object, Object[] expectedArguments) {
            this.object = object;
            this.expectedArguments = expectedArguments;
        }

        public T getObject() {
            return object;
        }

        public Object[] getExpectedArguments() {
            return expectedArguments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Holder<?> holder = (Holder<?>) o;

            return new EqualsBuilder()
                    .append(object, holder.object)
                    .append(expectedArguments, holder.expectedArguments)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(object)
                    .append(expectedArguments)
                    .toHashCode();
        }
    }

}
