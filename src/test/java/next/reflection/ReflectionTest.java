package next.reflection;

import com.google.common.base.Defaults;
import next.util.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    ParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    @Test
    @DisplayName("클래스 정보를 출력하는 테스트")
    public void showClass() throws Exception {
        Class<Question> clazz = Question.class;

        log.debug("name: {}", clazz.getName());
        log.debug("modifiers: {}", clazz.getModifiers());

        constructor();
        fields();
        methods();
    }

    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();

        log.debug("----------------------- constructor starts ------------------------");
        for (Constructor constructor : constructors) {
            log.debug("constructor name: {}", constructor.getName());

            Class[] parameterTypes = constructor.getParameterTypes();
            log.debug("parameter length : {}", parameterTypes.length);

            for (Class paramType : parameterTypes) {
                log.debug("param type : {}", paramType);
            }
            log.debug("");
        }
        log.debug("----------------------- constructor ends ------------------------");
    }

    public void fields() {
        Class<Question> clazz = Question.class;
        log.debug("----------------------- fields starts ------------------------");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            log.debug("name: {}", field.getName());
            log.debug("modifier : {}", field.getModifiers());
            log.debug("");
        }
        log.debug("------------------------- fields end -------------------------");
    }

    private void methods() {
        Class<Question> clazz = Question.class;
        Method[] methods = clazz.getDeclaredMethods();
        log.debug("----------------------- methods starts ------------------------");
        for (Method method : methods) {
            log.debug("name: {}", method.getName());
            log.debug("modifier : {}", method.getModifiers());

            Class[] parameterTypes = method.getParameterTypes();
            log.debug("parameter length : {}", parameterTypes.length);

            for(Class paramType : parameterTypes) {
                log.debug("param type: {}", paramType);
            }
            log.debug("");
        }
        log.debug("----------------------- methods starts ------------------------");
    }

    @Test
    @DisplayName("private 필드에 값을 셋팅하는 테스트")
    public void privateFieldAccess() {
        Class<Student> clazz = Student.class;
        log.debug("class name: {}", clazz.getName());

        Student student = new Student();
        String name = "동엽";
        int age = 42;

        setFields(clazz.getDeclaredFields(), student, name, age);

        assertThat(student.getName()).isEqualTo(name);
        assertThat(student.getAge()).isEqualTo(age);
    }

    private void setFields(Field[] declaredFields, Student student, String name, int age) {
        if (declaredFields.length <= 0) {
            return;
        }

        Arrays.stream(declaredFields)
            .filter(this::isPrivateModifier)
            .forEach(field -> {
                field.setAccessible(true);
                try {
                    setField(field, student, name, age);
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
    }

    private boolean isPrivateModifier(Field field) {
        return field.getModifiers() == Modifier.PRIVATE;
    }

    private void setField(Field field, Student student, String name, int age) throws IllegalAccessException {
        switch(field.getName()) {
            case "name":
                field.set(student, name);
                return;

            case "age":
                field.set(student, age);
                return;
        }
    }

    @Test
    @DisplayName("인자가 있는 생성자를 통해 인스턴스를 생성하는 테스트")
    public void createInstanceByConstructorWithArguments() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getDeclaredConstructors();

        for (Constructor constructor : constructors) {
            List<Object> arguments = getConstructorArguments(constructor);

            Object newInstance = constructor.newInstance(arguments.toArray());
            log.debug("newInstance: {}", StringUtils.toPrettyJson(newInstance));

            assertThat(newInstance).isNotNull();
        }
    }

    private List<Object> getConstructorArguments(Constructor constructor) {
        List<Object> arguments = Lists.newArrayList();
        Class[] parameterTypes = constructor.getParameterTypes();
        String[] parameterNames = nameDiscoverer.getParameterNames(constructor);

        log.debug("parameterNames: {}", StringUtils.toPrettyJson(parameterNames));

        for (int i = 0; i < parameterTypes.length; i++) {
            Class paramType = parameterTypes[i];

            if (paramType.equals(String.class)) {
                arguments.add(StringUtils.getOrDefault(parameterNames[i], paramType.getName()));
                continue;
            }

            arguments.add(Defaults.defaultValue(paramType));
        }

        return arguments;
    }
}
