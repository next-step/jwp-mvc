package core.mvc.tobe.resolver;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public class ModelArgumentResolver implements HandlerMethodArgumentResolver {
    private static final ParameterNameDiscoverer NAME_DISCOVERER = new LocalVariableTableParameterNameDiscoverer();

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.isModelType();
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> parameterType = methodParameter.getType();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Constructor<?> constructor = findConstructor(parameterType, parameterMap);

        Class<?>[] fieldTypes = constructor.getParameterTypes();
        String[] fieldNames = NAME_DISCOVERER.getParameterNames(constructor);

        verifyFields(fieldTypes, fieldNames);

        Object[] arguments = getValues(request, fieldTypes, fieldNames);
        return constructor.newInstance(arguments);
    }

    private Object[] getValues(HttpServletRequest request, Class<?>[] fieldTypes, String[] fieldNames) {
        return IntStream.range(0, fieldTypes.length)
                .mapToObj(idx -> TypeConverter.convert(fieldTypes[idx], request.getParameter(fieldNames[idx])))
                .toArray();
    }

    private void verifyFields(Class<?>[] fieldTypes, String[] fieldNames) {
        verifyNull(fieldTypes, fieldNames);
        verifyLengthEqual(fieldTypes, fieldNames);
    }

    private void verifyLengthEqual(Class<?>[] fieldTypes, String[] fieldNames) {
        if (fieldTypes.length != fieldNames.length) {
            throw new IllegalArgumentException("필드 타입 갯수와 필드 이름은 반드시 같아야 합니다.");
        }
    }

    private void verifyNull(Class<?>[] fieldTypes, String[] fieldNames) {
        if (fieldTypes == null || fieldNames == null) {
            throw new IllegalArgumentException("매핑 하고자 하는 객체의 필드 타입 혹은 필드 이름이 반드시 1개 이상 존재해야 합니다.");
        }
    }

    private static Constructor<?> findConstructor(Class<?> parameterType, Map<String, String[]> parameterMap) {
        return Arrays.stream(parameterType.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == parameterMap.size())
                .findAny()
                .orElseThrow(() -> new IllegalStateException("주어진 필드에 대한 객체의 생성자가 존재하지 않습니다."));
    }
}
