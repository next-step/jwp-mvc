package core.mvc.tobe;

import core.annotation.web.PathVariable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentResolvers {

    private List<AbstractArgumentResolver> abstractArgumentResolvers;
    private Class clazz;

    public ArgumentResolvers(Class clazz) {
        this.abstractArgumentResolvers = new ArrayList<>();
        this.clazz = clazz;
    }

    public void add(Method method) {
        if (isPathVariable(method)) {
            return;
        }
        abstractArgumentResolvers.add(new BasicTypeArgumentResolver(clazz, method));
    }

    private boolean isPathVariable(Method method) {
        return Arrays.stream(method.getParameterAnnotations())
                .flatMap(Arrays::stream)
                .anyMatch(annotation1 -> annotation1 instanceof PathVariable);
    }

}
