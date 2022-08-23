package core.mvc.tobe;

import java.util.Arrays;

public enum ParameterTypeEnum {
    STRING(String.class, "java.lang.String"),
    INT_PRIMITIVE(int.class, "int"),
    LONG_PRIMITIVE(long.class, "long"),
    INTEGER(Integer.class, "java.lang.Integer"),
    LONG(Long.class, "java.lang.Long");

    private final String className;

    ParameterTypeEnum(Class<?> type, String className) {
        this.className = className;
    }

    public static Object casting(String value, Class<?> clazz) {
        switch (type(clazz)) {
            case INT_PRIMITIVE: return Integer.parseInt(value);
            case LONG_PRIMITIVE: return Long.parseLong(value);
            case INTEGER: return Integer.valueOf(value);
            case LONG: return Long.valueOf(value);
            default: return value;
        }
    }

    public static ParameterTypeEnum type(Class<?> clazz) {
        return Arrays.stream(values()).filter(type -> type.className.equals(clazz.getName())).findFirst().orElse(STRING);
    }
}
