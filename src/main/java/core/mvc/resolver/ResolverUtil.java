package core.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.TypeVariable;
import java.util.stream.IntStream;

public class ResolverUtil {

    public static Object convertPrimitiveType(Class<?> parameterType, String object) {

        if (parameterType.equals(int.class))
            return Integer.parseInt(object);
        else if (parameterType.equals(double.class))
            return Double.parseDouble(object);
        else if (parameterType.equals(long.class))
            return Long.parseLong(object);
        else if (parameterType.equals(float.class))
            return Float.parseFloat(object);

        return object;
    }

    public static Object getObject(HttpServletRequest request, Class<?> type, Field[] declaredFields) {
        Object[] objects = new Object[declaredFields.length];
        int i = 0;
        for (Field field : declaredFields) {
            objects[i++] = request.getParameter(field.getName());
        }

        Constructor<?>[] declaredConstructors = type.getDeclaredConstructors();
        for (Constructor c : declaredConstructors) {
            if (isValidConstructor(objects, c)) {
                return getObjectByConstructor(c, objects);
            }
        }

        throw new IllegalArgumentException("@RequestBody 객체를 생성할 생성자가 없습니다");
    }

    /**
     * 객체를 생성할 수 있는 생성자가 존재하는지 확인한다.
     *
     * @param objects
     * @param c
     * @return
     */
    private static boolean isValidConstructor(Object[] objects, Constructor c) {
        TypeVariable[] typeParameters = c.getTypeParameters();

        boolean result = IntStream
                .range(0, typeParameters.length)
                .allMatch(index -> typeParameters[index].equals(objects[index]));

        return result;
    }

    private static Object getObjectByConstructor(Constructor constructor, Object[] objects) {
        Object o = null;
        try {
            o = constructor.newInstance(objects);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
}
