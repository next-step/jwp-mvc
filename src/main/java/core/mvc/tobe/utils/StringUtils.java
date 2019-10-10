package core.mvc.tobe.utils;

import java.util.Objects;

public final class StringUtils {

    private StringUtils() { }

    public static boolean isBlank(final String value) {
        return Objects.isNull(value) || value.isBlank();
    }

    public static boolean isNotBlank(final String value) {
        return !isBlank(value);
    }
}
