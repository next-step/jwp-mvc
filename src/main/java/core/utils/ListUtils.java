package core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {

    public static <T> List<T> newArrayList(T... elements) {
        return Arrays.stream(elements)
                .collect(Collectors.toList());
    }
}
