package core.mvc.tobe.method;

import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MethodParameters {

    private final List<MethodParameter> parameters;

    private MethodParameters(List<MethodParameter> parameters) {
        Assert.notEmpty(parameters, "parameters는 비어선 안됩니다.");
        this.parameters = Collections.unmodifiableList(parameters);
    }

    public static MethodParameters from(Method method) {
        Assert.notNull(method, "method가 null이어선 안됩니다.");
        return new MethodParameters(
                IntStream.range(0, method.getParameterCount())
                        .mapToObj(i -> MethodParameter.of(method, method.getParameters()[0]))
                        .collect(Collectors.toList())
        );
    }

    public <T> T[] mapToArray(Function<? super MethodParameter, T> mapper, IntFunction<T[]> generator) {
        return parameters.stream()
                .map(mapper)
                .toArray(generator);
    }
}
