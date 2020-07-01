package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    /**
     * java.lang.Class
     * String getName() : 패키지 + 클래스 이름을 반환한다.
     * int getModifiers() : 클래스의 접근 제어자를 숫자로 반환한다.
     * Field[] getFields() : 접근 가능한 public 필드 목록을 반환한다.
     * Field[] getDeclaredFields() : 모든 필드 목록을 반환한다.
     * Constructor[] getConstructors() : 접근 가능한 public 생성자 목록을 반환한다.
     * Constructor[] getDeclaredConstructors() : 모든 생성자 목록을 반환한다.
     * Method[] getMethods() : 부모 클래스, 자신 클래스의 접근 가능한 public 메서드 목록을 반환한다.
     * Method[] getDeclaredMethods() : 모든 메서드 목록을 반환한다.
     *
     * java.lang.refelct.Constructor
     * String getName() : 생성자 이름을 반환한다.
     * int getModifiers() : 생성자의 접근 제어자를 숫자로 반환한다.
     * Class[] getParameterTypes() : 생성자 패러미터의 데이터 타입을 반환한다.
     *
     * java.lang.refelct.Field
     * String getName() : 필드 이름을 반환한다.
     * int getModifiers() : 필드의 접근 제어자를 숫자로 반환한다.
     *
     * java.lang.refelct.Method
     * String getName() : 메서드 이름을 반환한다.
     * int getModifiers() : 메서드의 접근 제어자를 숫자로 반환한다.
     * Class[] getParameterTypes() : 메서드 패러미터의 데이터 타입을 반환한다.
     */
    @Test
    public void showClass() {
        // Question 클래스의 모든 필드, 생성자, 메소드에 대한 정보를 출력한다.
        Class<Question> clazz = Question.class;
        logger.debug(clazz.getName());
        // 모든 필드 정보
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(s -> logger.debug("Field Modifiers number : " + s.getModifiers()
                        + " / Field type : " + s.getType().getName()
                        + " / Field name : " + s.getName()));
        // 모든 생성자 정보
        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach(s -> {
                    List<String> parameterTypes = Arrays.stream(s.getParameterTypes())
                            .map(mn -> mn.getName())
                            .collect(Collectors.toList());
                    logger.debug("Constructor Modifiers number : " + s.getModifiers()
                            + " / Constructor name : " + s.getName()
                            + " / Constructor parameters type : " + parameterTypes.toString());
                });
        // 모든 메소드 정보
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(s -> {
                    List<String> parameterTypes = Arrays.stream(s.getParameterTypes())
                            .map(mn -> mn.getName())
                            .collect(Collectors.toList());
                    logger.debug("Method Modifiers number : " + s.getModifiers()
                            + " / Method return type : " + s.getReturnType().getName()
                            + " / Method name : " + s.getName()
                            + " / Method parameters type : " + parameterTypes.toString());
                });
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void constructor() throws Exception {
        Class<Question> clazz = Question.class;
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            Class[] parameterTypes = constructor.getParameterTypes();
            logger.debug("paramer length : {}", parameterTypes.length);
            for (Class paramType : parameterTypes) {
                logger.debug("param type : {}", paramType);
            }
        }
    }
}
