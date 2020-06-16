package core.mvc.param.extractor.complex;

import core.exception.ParameterNotFoundException;
import core.mvc.param.Parameter;
import core.mvc.param.extractor.ValueExtractor;
import core.mvc.param.extractor.simple.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

// 기본 타입의 객체가 아닌 사용자가 생성한 객체를 리플렉션을 이용해 생성 후 리턴
// 기본 생성자가 있어야 한다. 기본 생성자 사용안할 경우 복잡해짐...
public class ComplexValueExtractor implements ValueExtractor {
    private static final Logger logger = LoggerFactory.getLogger(ComplexValueExtractor.class);

    @Override
    public Object extract(Parameter parameter, HttpServletRequest request) {
        Class<?> clazz = parameter.getTypeClass();
        Object instance = newInstance(clazz);

        if (instance == null) {
            return null;
        }

        try {
            setFields(request, clazz, instance);
        } catch (Exception e) {
            logger.debug("Fail to create instance : " + parameter.getName());

            return null;
        }

        return instance;
    }

    private void setFields(HttpServletRequest request, Class<?> clazz, Object instance) {
        Arrays.stream(clazz.getDeclaredFields())
                .forEach(field -> setPrivateFieldValue(instance, field, request));
    }

    // 새 인스턴스 생성
    private Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                InvocationTargetException | NoSuchMethodException e) {
            logger.debug("Fail to create new instance of [{}]", clazz.getName());
        }

        return null;
    }

    // 객체의 private 필드에 값 셋팅
    private void setPrivateFieldValue(Object instance, Field field, HttpServletRequest request) {
        try {
            String parameter = findParameter(field, request);

            field.setAccessible(true);
            field.set(instance, TypeParser.parse(field.getType(), parameter));
        } catch (IllegalAccessException e) {
            logger.debug("Fail to set value at private field [{}]", e.getMessage());
        }
    }

    private String findParameter(Field field, HttpServletRequest request) {
        String parameter = request.getParameter(field.getName());

        if (parameter == null) {
            throw new ParameterNotFoundException(field.getName());
        }

        return parameter;
    }
}
