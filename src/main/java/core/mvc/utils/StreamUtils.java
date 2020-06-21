package core.mvc.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class StreamUtils {

    public static <A, B>List<Pair<A, B>> zip(List<A> aList, List<B> bList) {
        return IntStream.range(0, Math.min(aList.size(), bList.size()))
                .mapToObj(i -> Pair.of(aList.get(i), bList.get(i)))
                .collect(Collectors.toList());
    }
}
