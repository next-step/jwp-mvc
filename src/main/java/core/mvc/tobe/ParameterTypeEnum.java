package core.mvc.tobe;

import java.util.Arrays;

public enum ParameterTypeEnum {
    STRING(String.class, "java.lang.String"),
    INT_PRIMITIVE(int.class, "int"),
    LONG_PRIMITIVE(long.class, "long"),
    INTEGER(Integer.class, "java.lang.Integer"),
    LONG(Long.class, "java.lang.Long"),
    OBJECT(Object.class, "java.lang.Object");

    private final String className;

    ParameterTypeEnum(Class<?> type, String className) {
        this.className = className;
    }

    public static Object casting(Object value, Class<?> clazz) {
        if (type(clazz) == OBJECT) {
            return value;
        }

        String stringValue = String.valueOf(value);
        switch (type(clazz)) {
            case INT_PRIMITIVE: return Integer.parseInt(stringValue);
            case LONG_PRIMITIVE: return Long.parseLong(stringValue);
            case INTEGER: return Integer.valueOf(stringValue);
            case LONG: return Long.valueOf(stringValue);
            default: return stringValue;
        }
    }

    public static ParameterTypeEnum type(Class<?> clazz) {
        return Arrays.stream(values()).filter(type -> type.className.equals(clazz.getName())).findFirst().orElse(OBJECT);
    }
}
