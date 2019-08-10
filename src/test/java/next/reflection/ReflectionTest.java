package next.reflection;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    public void showClass() {
        Class<Question> clazz = Question.class;
        logger.debug(String.format("ClassName: %s", clazz.getName()));
        logger.debug("");
        // 필드
        logger.debug("[Fields]");
        final Field[] fields = clazz.getDeclaredFields();
        for(Field f : fields) {
            showField(f);
        }
        logger.debug("");
        // 생성자
        logger.debug("[Constructors]");
        final Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for(Constructor ctor : constructors) {
            showConstructor(ctor);
        }
        logger.debug("");
        // 메서드
        logger.debug("[Methods]");
        final Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods) {
            showMethod(method);
        }
    }

    private void showField(Field f) {
        final String name = f.getName();
        final int modifiers = f.getModifiers();
        String mod = fromModifier(modifiers);
        final Class<?> type = f.getType();
        final String typeName = type.getName();
        logger.debug(String.format("Field: %s %s %s;", mod, typeName, name));
    }

    private void showConstructor(Constructor ctor) {
        final String name = ctor.getName();
        final int modifiers = ctor.getModifiers();
        final String mod = fromModifier(modifiers);
        final Parameter[] parameters = ctor.getParameters();
        final int paramCount = ctor.getParameterCount();
        StringBuilder sb = new StringBuilder();
        logger.debug(String.format("ConstructorName: %s", name));
        logger.debug(String.format("ConstructorModifier: %s", mod));
        logger.debug(String.format("ConstructorParams : %s", paramCount));
        for(Parameter param : parameters) {
            showParameter(param);
        }
    }

    private void showMethod(Method method) {
        final String name = method.getName();
        final int modifiers = method.getModifiers();
        final String mod = fromModifier(modifiers);
        final Class<?> returnType = method.getReturnType();
        final Parameter[] parameters = method.getParameters();
        final int paramCount = method.getParameterCount();
        logger.debug(String.format("MethodName: %s", name));
        logger.debug(String.format("MethodModifier: %s", mod));
        logger.debug("MethodsParams");
        logger.debug(String.format("MethodsParams : %s", paramCount));
        for(Parameter param : parameters) {
            showParameter(param);
        }
    }

    private void showParameter(Parameter param) {
        final Class<?> paramType = param.getType();
        final String paramName = param.getName();
        logger.debug(String.format("ParamType: %s, ParamName: %s", paramType, paramName));
    }

    private String fromModifier(int mod) {
        return mod == 0 ? "" : Modifier.toString(mod);
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
